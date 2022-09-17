package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO { // Mismas propiedades que la clase Client
    private long id;
    private String name, lastName, email;
    private int age;
    private Set<AccountDTO> accountSet = new HashSet<>();
    private Set<ClientLoanDTO> loanSet = new HashSet<>();
    private Set<CardDTO> cardSet = new HashSet<>();

    public ClientDTO(){}
    public ClientDTO(Client cliente){
        this.id= cliente.getId();
        this.name = cliente.getName();
        this.lastName = cliente.getLastName();
        this.age = cliente.getAge();
        this.email = cliente.getEmail();
        this.accountSet = cliente.getAccountSet().stream().filter(account -> !account.isDeleted()).map(account-> new AccountDTO(account)).collect(toSet());
        this.loanSet = cliente.getLoanSet().stream().map(loan -> new ClientLoanDTO(loan)).collect(toSet());
        this.cardSet = cliente.getCardSet().stream().filter(card -> !card.isDeleted()).map(card ->new CardDTO(card)).collect(toSet());
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public Set<AccountDTO> getAccountSet() {
        return accountSet;
    }

    public Set<ClientLoanDTO> getLoanSet() {
        return loanSet;
    }

    public Set<CardDTO> getCardSet() {
        return cardSet;
    }
}
