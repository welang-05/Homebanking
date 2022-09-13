package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountServices;
import com.mindhub.homebanking.Services.CardServices;
import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.Services.TransactionServices;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
public class TransactionController {

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private CardServices cardServices;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String sourceAccount, @RequestParam String destinyAccount, @RequestParam String description, Authentication autenticacion) {
        if (amount == 0 || sourceAccount.isEmpty() || destinyAccount.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount.equals(destinyAccount)) {
            return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN);
        }

        Account sAccount = accountServices.findAccountByNumber(sourceAccount);
        Account dAccount = accountServices.findAccountByNumber(destinyAccount);

        if (sAccount == null) {
            return new ResponseEntity<>("The source account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (dAccount == null) {
            return new ResponseEntity<>("The destiny account doesn't exist", HttpStatus.FORBIDDEN);
        }
        Client client = clientServices.findClientByEmail(autenticacion.getName());
        if (accountServices.findAccountByTitularAndNumber(client, sourceAccount) == null) {
            return new ResponseEntity<>("The origin account doesn't belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (sAccount.getBalance() < amount) {
            return new ResponseEntity<>("You don't have enough funds", HttpStatus.FORBIDDEN);
        }

        Transaction tranDebit = new Transaction(sAccount, TransactionType.DEBIT, -amount, description, LocalDateTime.now());
        sAccount.setBalance(sAccount.getBalance() - amount);
        CardUtils.updateCardQuota(sAccount, cardServices);

        Transaction tranCredit = new Transaction(dAccount, TransactionType.CREDIT, amount, description, LocalDateTime.now());
        dAccount.setBalance(dAccount.getBalance() + amount);
        CardUtils.updateCardQuota(sAccount, cardServices);

        transactionServices.saveTransaction(tranDebit);
        transactionServices.saveTransaction(tranCredit);
//        accountServices.saveAccount(sAccount);
//        accountServices.saveAccount(dAccount);

        return new ResponseEntity<>("Transaction completed", HttpStatus.CREATED);

    }

}
