package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id;

    private TransactionType type;
    private double amount, currentBalance;
    private String description;
    private LocalDateTime date;
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creditCard_id")
    private Card card;

    public Transaction(){}

    public Transaction(Account cuenta, TransactionType tipo, double cantidad, String description, LocalDateTime fecha){
        this.account = cuenta;
        this.type = tipo;
        this.amount = cantidad;
        this.currentBalance = cuenta.getBalance()+cantidad;
        this.description = description;
        this.date = fecha;
        this.deleted = false;
    }

    public Transaction(Account cuenta, TransactionType tipo, double cantidad, LocalDateTime fecha){
        this.account= cuenta;
        this.type = tipo;
        this.amount = cantidad;
        this.currentBalance = cuenta.getBalance()+cantidad;
        this.date = fecha;
        this.deleted = false;
    }

    public Transaction(Card creditCard, TransactionType tipo, double cantidad, String description, LocalDateTime fecha){
        this.card = creditCard;
        this.type = tipo;
        this.amount = cantidad;
        this.currentBalance = card.getCardQuota();
        this.description = description;
        this.date = fecha;
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public Card getCard() {
        return card;
    }
    public void setCard(Card card) {
        this.card = card;
    }
}
