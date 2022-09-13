package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Services.AccountServices;
import com.mindhub.homebanking.Services.CardServices;
import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.Services.TransactionServices;
import com.mindhub.homebanking.dto.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
public class CardController {

    @Autowired
    private CardServices cardServices;

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private TransactionServices transactionServices;

    //Método para crear card
    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> newCard(@RequestParam CardColor cardColor, @RequestParam CardType cardType, String associatedAccount, Authentication autenticacion) {

        if (cardColor == null || cardType == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Client client = clientServices.findClientByEmail(autenticacion.getName());
        String cardNumber = "";
        if (cardServices.findCardByColorAndTypeAndTitularAndDeleted(cardColor, cardType, client, false) != null) {
            return new ResponseEntity<>("You already have a " + cardColor + " " + cardType + " card", HttpStatus.FORBIDDEN);
        }
        boolean noRepeat = false;
        while (!noRepeat) {
            cardNumber = CardUtils.getRandomNumber(4) + "  " + CardUtils.getRandomNumber(4) + "  " + CardUtils.getRandomNumber(4) + "  " + CardUtils.getRandomNumber(4);
            if (cardServices.findCardByNumber(cardNumber) == null) {
                noRepeat = true;
            }
        }

        if (cardType == CardType.CREDIT) {
            Card cardCredit = new Card(cardNumber, Integer.parseInt(CardUtils.getRandomNumber(3)), LocalDate.now(), LocalDate.now().plusYears(5), client.getName() + " " + client.getLastName(), cardColor, cardType, client);
            if (cardColor == CardColor.SILVER) {
                cardCredit.setCardQuota(50000);
            } else if (cardColor == CardColor.GOLD) {
                cardCredit.setCardQuota(150000);
            } else if (cardColor == CardColor.TITANIUM) {
                cardCredit.setCardQuota(400000);
            }
            cardServices.saveCard(cardCredit);

        } else if (cardType == CardType.DEBIT) {
            Account account = accountServices.findAccountByNumber(associatedAccount);
            Card cardDebit = new Card(cardNumber, Integer.parseInt(CardUtils.getRandomNumber(3)), LocalDate.now(), LocalDate.now().plusYears(5), client.getName() + " " + client.getLastName(), cardColor, cardType, client, account);
            cardDebit.setCardQuota(account.getBalance());
            cardServices.saveCard(cardDebit);
        }

        return new ResponseEntity<>("new " + cardType + " card created", HttpStatus.CREATED);
    }

    //Método para eliminar cards
    @PatchMapping("/api/clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String number, Authentication authentication) {

        if (number.length() < 16) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (cardServices.findCardByNumber(number) == null) {

            return new ResponseEntity<>("The card doesn't exist", HttpStatus.FORBIDDEN);

        }

        Client client = clientServices.findClientByEmail(authentication.getName());

        if (client.getCardSet().stream().filter(card -> card.getNumber().equals(number)).findFirst().orElse(null) == null) {

            return new ResponseEntity<>("The card doesn't belong to the authenticated user", HttpStatus.FORBIDDEN);

        }

        Card card = cardServices.findCardByNumber(number);

        for(Transaction transaction: card.getTransactions()){
            transaction.setDeleted(true);
        }

        card.setDeleted(true);

        cardServices.saveCard(card);

        return new ResponseEntity<>("Card number: " + number + " successfully deleted", HttpStatus.OK);

    }

    //Método utilización de tarjetas compra
    @Transactional
    @PostMapping("/api/post_net")
    public ResponseEntity<Object> paymentService(@RequestBody PaymentDTO paymentDTO, Authentication authentication) {

        if (paymentDTO == null) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        Card card = cardServices.findCardByNumber(paymentDTO.getCardNumber());

        if (card == null) {
            return new ResponseEntity<>("The card doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (cardServices.findCardsByTitular(clientServices.findClientByEmail(authentication.getName()))==null) {
            return new ResponseEntity<>("The card doesn't  belong to the logged client", HttpStatus.FORBIDDEN);
        }

        if (card.getCardQuota() < paymentDTO.getAmount()) {
            return new ResponseEntity<>("The card doesn't have enough funds", HttpStatus.FORBIDDEN);
        }

        if (card.getCvv() != paymentDTO.getCvv()) {
            return new ResponseEntity<>("The CVV doesn't march with the database", HttpStatus.FORBIDDEN);
        }

        if (card.getThruDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("The card you are using is expired", HttpStatus.FORBIDDEN);
        }


        //TODO OK
        if (card.getCardType() == CardType.CREDIT) {
            card.setCardQuota(card.getCardQuota() - paymentDTO.getAmount());
            Transaction tranCredit = new Transaction(card, TransactionType.DEBIT, -paymentDTO.getAmount(), paymentDTO.getDescription(), LocalDateTime.now());

            transactionServices.saveTransaction(tranCredit);

        } else if (card.getCardType() == CardType.DEBIT) {
            Account account = card.getAccount();
            Transaction tranDebit = new Transaction(account, TransactionType.DEBIT, -paymentDTO.getAmount(), paymentDTO.getDescription(), LocalDateTime.now());
            account.setBalance(account.getBalance() - paymentDTO.getAmount());
            card.setCardQuota(account.getBalance());

            accountServices.saveAccount(account);
            transactionServices.saveTransaction(tranDebit);
        }

        cardServices.saveCard(card);

        return new ResponseEntity<>("Payment made successfully", HttpStatus.ACCEPTED);
    }

    //Método para pagar tarjetas de crédito
    @Transactional
    @PostMapping("/api/pay_card")
    public ResponseEntity<Object> payCreditCard(@RequestParam String number, @RequestParam double amount, Authentication authentication) {

        if (number == null || amount==0) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        Card card = cardServices.findCardByNumber(number);

        if (card == null) {
            return new ResponseEntity<>("The card doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(card.getCardType()==CardType.DEBIT){
            return new ResponseEntity<>("The card number you entered is not a credit card", HttpStatus.FORBIDDEN);
        }

        if(card.getCardColor()==CardColor.SILVER && card.getCardQuota()==50000 || card.getCardColor()==CardColor.GOLD && card.getCardQuota()==150000 || card.getCardColor()==CardColor.TITANIUM && card.getCardQuota()==500000 ){
            return new ResponseEntity<>("You don't have debt in this credit card", HttpStatus.FORBIDDEN);
        }

        if (clientServices.findClientByEmail(authentication.getName()).getCardSet().contains(card)) {
            return new ResponseEntity<>("The card doesn't  belong to the logged client", HttpStatus.FORBIDDEN);
        }

        card.setCardQuota(card.getCardQuota()+amount);
        Transaction cardPayment = new Transaction(card,TransactionType.CREDIT,amount,"",LocalDateTime.now());
        transactionServices.saveTransaction(cardPayment);
        cardServices.saveCard(card);

        return new ResponseEntity<>("Payment made successfully", HttpStatus.ACCEPTED);

    }

}
