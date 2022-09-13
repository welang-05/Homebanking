package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id; //Única para cada objeto creado con esta clase

    private double amount, interest; //Monto del préstamo que toma el cliente en particular, además de interés
    private int payments;

    @ManyToOne(fetch = FetchType.EAGER) //Relación de la clase ClientLoan será de muchos a uno, muchas ClientLoan
    // para un cliente
    @JoinColumn(name = "client_id") //Se define una columna que relacionará ClientLoan con Client, clave foranea
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER) //Relación de la clase ClientLoan será de muchos a uno, muchas ClientLoan
    // para un tipo de Loan
    @JoinColumn(name = "loan_id") //Se define una columna que relacionará las cuentas con los clientes, clave foreana
    private Loan loan; //Se define una columna que relacionará ClientLoan con Loan

    public ClientLoan() {
    }

    public ClientLoan(double amount, int payments, Client client, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
        this.interest = loan.getInterest();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterest() {
        return interest;
    }
    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getPayments() {
        return payments;
    }
    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }
    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
