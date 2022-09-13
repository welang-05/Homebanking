package com.mindhub.homebanking.dto;


public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payments;
    private String accountNumber;

    public LoanApplicationDTO(){}

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
