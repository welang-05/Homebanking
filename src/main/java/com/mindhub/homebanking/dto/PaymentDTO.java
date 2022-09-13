package com.mindhub.homebanking.dto;

public class PaymentDTO {

    private String cardNumber, description;

    private int cvv;

    private double amount;

    public PaymentDTO() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }
}
