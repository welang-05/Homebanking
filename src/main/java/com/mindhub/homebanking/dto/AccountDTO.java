package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private List<TransactionDTO> transactionList;
//    private Set<String> cardNumberSet = new HashSet<>();
    private Set<CardDTO> cardSet = new HashSet<>();

    public AccountDTO(){}

    public AccountDTO (Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.creationDate = account.getCreationDate();
        this.transactionList = account.getTransactionSet().stream().filter(transaction -> !transaction.isDeleted()).map(TransactionDTO::new).collect(Collectors.toList());
        this.cardSet = account.getCardSet().stream().filter(card -> !card.isDeleted()).map(CardDTO::new).collect(toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

    public Set<CardDTO> getCardSet() {
        return cardSet;
    }

}
