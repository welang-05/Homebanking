package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountServices;
import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.Services.TransactionServices;
import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private AccountServices accountServices;

    @Autowired
    private TransactionServices transactionServices;


    @GetMapping("/api/accounts") //Asociar petición en esa dirección al método de abajo
    public List<AccountDTO> getAccounts() {
        return accountServices.getAllAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id) {
        return new AccountDTO(accountServices.findAccountById(id));
    }

    //Método para crear cuentas
    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> addAccount(@RequestParam AccountType accountType, Authentication autenticacion) {
        Client client = clientServices.findClientByEmail(autenticacion.getName());

        if (client.getAccountSet().stream().filter(account -> !account.isDeleted()).collect(Collectors.toSet()).size() >= 3) {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }
        String accountNumber = "";
        boolean noRepeat = false;
        while (!noRepeat) {
            accountNumber = "VIN" + CardUtils.getRandomNumber(8);
            Account account = accountServices.findAccountByNumber(accountNumber);
            if (account == null) {
                noRepeat = true;
            }
        }
        accountServices.saveAccount(new Account(accountNumber, LocalDateTime.now(), 0,accountType, client));
        return new ResponseEntity<>("New account created successfully",HttpStatus.CREATED);
    }

    //Método para eliminar cuentas
    @Transactional
    @PatchMapping("/api/clients/currents/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam String accountToDelete, @RequestParam String destinyAccount, Authentication authentication){

        if(accountToDelete.length()<8){
            return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
        }

        Account accountOrigin = accountServices.findAccountByNumber(accountToDelete);

        if(accountOrigin==null){
            return new ResponseEntity<>("The account you want to delete doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin.getBalance()>0 && destinyAccount.length()<8){
            return new ResponseEntity<>("Missing destiny account",HttpStatus.FORBIDDEN);
        }

        Client client = clientServices.findClientByEmail(authentication.getName());

        if (client.getAccountSet().stream().filter(account -> account.getNumber().equals(accountToDelete)).findFirst().orElse(null)==null){
            return new ResponseEntity<>("The account doesn't belong to the authenticated user", HttpStatus.FORBIDDEN);
        }

        Account accountDestiny = accountServices.findAccountByNumber(destinyAccount);

        if(accountDestiny==null && accountOrigin.getBalance()>0){
            return new ResponseEntity<>("The account where you want to transfer your founds doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin.getBalance()>0){
            Transaction tranCredit = new Transaction(accountDestiny, TransactionType.CREDIT, accountOrigin.getBalance(),"Las transaction from: " + accountToDelete , LocalDateTime.now());
            accountDestiny.setBalance(accountDestiny.getBalance() + accountOrigin.getBalance());
            Transaction tranDebit = new Transaction(accountOrigin, TransactionType.DEBIT, -accountOrigin.getBalance(), "Las transaction to: " + destinyAccount, LocalDateTime.now());
            accountOrigin.setBalance(0.0);
            tranDebit.setDeleted(true);

            transactionServices.saveTransaction(tranDebit);
            transactionServices.saveTransaction(tranCredit);
        }

        accountOrigin = accountServices.findAccountByNumber(accountToDelete);

        for (Transaction transaction : accountOrigin.getTransactionSet()) {
            transaction.setDeleted(true);
        }
        for (Card card : accountOrigin.getCardSet()) {
            card.setDeleted(true);
        }

        accountOrigin.setDeleted(true);

        accountServices.saveAccount(accountOrigin);

        return new ResponseEntity<>("Account deleted correctly",HttpStatus.OK);


    }



}
