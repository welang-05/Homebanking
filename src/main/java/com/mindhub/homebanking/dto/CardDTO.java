package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CardDTO {
    private long id;
    private String number;
    private int cvv;
    private LocalDate fromDate, thruDate;
    private String cardholder;
    private CardColor cardColor;
    private CardType cardType;
    private double cardQuota;
    private List<TransactionDTO> transactionList;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardholder = card.getCardHolder();
        this.cardColor = card.getCardColor();
        this.cardType = card.getCardType();
        this.cardQuota = card.getCardQuota();
        this.transactionList = card.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public double getCardQuota() {
        return cardQuota;
    }

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

}
