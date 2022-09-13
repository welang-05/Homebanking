package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountServices {

    List<Account> getAllAccounts();

    Account findAccountById(long id);

    Account findAccountByNumber(String number);

    Account findAccountByTitularAndNumber(Client client, String number);

    void saveAccount(Account account);

}
