package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity //Crear una tabla en la base de datos en base a esta clase

public class Account {

    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private AccountType accountType;
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER) //Relación de los clase account será muchos (cuentas)  a uno (cliente titular)
    @JoinColumn(name = "titular_id") //Se define una columna que relacionará las cuentas con los clientes (Compartirán id)
    private Client titular; //Se crea una propiedad con la clase Client, para que guarde los datos de este, una vez se le asigne uno

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)//Relación uno a muchos, una cuenta, muchos transacciones(Declarado abajo)
    private Set<Transaction> transactionSet = new HashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)//Relación uno a muchos, una cuenta, muchas tarjetas(Declarado abajo)
    private Set<Card> cardSet = new HashSet<>();

    public Account(){ }

    public Account(String numero, LocalDateTime creationTime, AccountType type){
        this.number= numero;
        this.creationDate = creationTime;
        this.balance = 0.0;
        this.accountType = type;
        this.deleted = false;
    } //Constructor 1
    public Account(String numero, LocalDateTime creationTime, double dinero, AccountType type){
        this.number = numero;
        this.creationDate = creationTime;
        this.balance = dinero;
        this.accountType = type;
        this.deleted = false;
    } //Constructor 2

    public Account(String numero, LocalDateTime creationTime, double dinero,AccountType type, Client client){
        this.number = numero;
        this.creationDate = creationTime;
        this.balance = dinero;
        this.titular = client;
        this.accountType = type;
        this.deleted = false;
    } //Constructor 3, entregándole directamente el cliente como argumento para que lo agregue a la cuenta que se está creando

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getTitular(){
        return titular;
    }
    public void setTitular(Client titular){
        this.titular = titular;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }
    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        this.transactionSet.add(transaction);
    }

    public Set<Card> getCardSet() {
        return cardSet;
    }
    public void setCardSet(Set<Card> cardSet) {
        this.cardSet = cardSet;
    }

    public void addCard(Card card){
        card.setAccount(this);
        this.cardSet.add(card);
    }

}
