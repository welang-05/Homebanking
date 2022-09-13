package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity //Crea una tabla en la base de datos
public class Card {
    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id;

    private int cvv;
    private LocalDate fromDate, thruDate;
    private String cardHolder, number;
    private CardColor cardColor;
    private CardType cardType;

    private double cardQuota; //Cupo de la tarjeta
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER) //Relación de la clase cards será mucho (cards) a uno (cliente titular)
    @JoinColumn(name = "titular_id") //Se define una columna que relacionará las tarjetas con los clientes
    private Client titular; //Se crea una propiedad con la clase Client, para que guarde los datos de este, una vez se le asigne uno a la tarjeta

    @ManyToOne(fetch = FetchType.EAGER) //Relación de la clase cards será mucho (cards) a uno (cuenta)
    @JoinColumn(name = "account_id") //Se define una columna que relacionará las tarjetas con las cuentas
    private Account account;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER) //Relación de tarjetas y transferencias será de uno a muchos
    private Set<Transaction> transactionSet;

    public Card() {}

    public Card(String number, int cvv, LocalDate fromDate, LocalDate thruDate, String cardHolder, CardColor cardColor,
                CardType cardType, Client titular, Account account) { //Constructor1 para tarjetas de débito, con una cuenta asociada
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = cardHolder;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.titular = titular;
        this.account = account;
        this.deleted = false;
    }

    public Card(String number, int cvv, LocalDate fromDate, LocalDate thruDate, String cardHolder, CardColor cardColor,
                CardType cardType, Client titular) { //Constructor2 para tarjetas de crédito, sin una cuenta asociada
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = cardHolder;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.titular = titular;
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardColor getCardColor() {
        return cardColor;
    }
    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public double getCardQuota() {
        return cardQuota;
    }
    public void setCardQuota(double cardQuota) {
        this.cardQuota = cardQuota;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Client getTitular() {
        return titular;
    }
    public void setTitular(Client titular) {
        this.titular = titular;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Transaction> getTransactions() {
        return transactionSet;
    }
    public void setTransactions(Set<Transaction> transactions) {
        this.transactionSet = transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setCard(this);
        this.transactionSet.add(transaction);
    }
}
