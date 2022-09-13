package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private TransactionType type;
    private double amount, currentBalance;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaccion){
        this.type = transaccion.getType();
        this.amount = transaccion.getAmount();
        this.currentBalance = transaccion.getCurrentBalance();
        this.description = transaccion.getDescription();
        this.date = transaccion.getDate();
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}
