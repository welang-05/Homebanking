package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount, interest;
    private List<Integer> payments;

    public LoanDTO(){ }

    public LoanDTO( Loan loan){
        this.id=loan.getId();
        this.name=loan.getName();
        this.maxAmount=loan.getMaxAmount();
        this.payments=loan.getPayments();
        this.interest=loan.getInterest();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public double getMaxAmount() {
        return maxAmount;
    }

    public double getInterest() {
        return interest;
    }
    public List<Integer> getPayments() {
        return payments;
    }
}

