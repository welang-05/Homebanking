package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id //La propiedad de tipo Id será la clave primaria de la propiedad definida abajo
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Le asigna valor a la Id
    @GenericGenerator(name = "native", strategy = "native") //Verifica que no haya una id similar para no repetirla
    private long id;
    private String name;
    private double maxAmount, interest;
    @ElementCollection
    @Column (name="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER) //Relación de la clase cliente será de 1 a muchos, un cliente y muchos préstamos
    private Set<ClientLoan> loanSet = new HashSet<>(); //Permite definir un set(grupo) de préstamos a un cliente

    public Loan() {}

    public Loan(String name, double maxAmount, double interest, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    public Loan(String name, double maxAmount, double interest, List<Integer> payments, ClientLoan clientLoan) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
        this.loanSet.add(clientLoan);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getLoanSet() {
        return loanSet;
    }
    public void setLoanSet(Set<ClientLoan> loanSet) {
        this.loanSet = loanSet;
    }

    public double getInterest() {
        return interest;
    }
    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void addClient(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        this.loanSet.add(clientLoan);
    }

    public List<Client> getClients(){
        return this.loanSet.stream().map(ClientLoan::getClient).collect(Collectors.toList());
    }

}
