package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import java.util.List;

//@DataJpaTest ---> Sirve para hacer test basados en JPA (por ejemplo se puede usar en Postgres)
@SpringBootTest // ---> Trabajamos aquí con H2
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;


//   Pruebas LoanRepository
    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }


    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

//  Pruebas ClientRepository
    @Test
    public void existClients(){

        List<Client> clients = clientRepository.findAll();

        assertThat(clients,is(not(empty())));

    }

    @Test
    public void existHankClient(){

        Client client = clientRepository.findByEmail("mr.scorpio@gmail.com");

        assertThat(client,notNullValue());

    }

//  Pruebas AccountRepository
    @Test
    public void existAccounts(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,is(not(empty())));

    }

    @Test
    public void existVIN001(){

        Account account = accountRepository.findByNumber("VIN001");

//        Account account = accountRepository.findById(Long.parseLong("1"));

        assertThat(account,notNullValue());

    }

//  Pruebas CardRepository
    @Test
    public void existCards(){

        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));

    }

    @Test
    public void existThisCard(){

        Client client = clientRepository.findByEmail("mr.scorpio@gmail.com");

        Card card = cardRepository.findByCardColorAndCardTypeAndTitular(CardColor.GOLD, CardType.DEBIT, client);

        assertThat(card, notNullValue());

    }

//  Pruebas TransactionRepository
    @Test
    public void existTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions,is(not(empty())));

    }

    @Test
    public void existSaveThisTransaction(){

        Account account = accountRepository.findByNumber("VIN001");

        transactionRepository.save(new Transaction(account, TransactionType.CREDIT, 1000, "Tercera", LocalDateTime.now()));

    }

}
