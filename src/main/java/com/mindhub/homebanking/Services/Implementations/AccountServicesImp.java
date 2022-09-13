package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.AccountServices;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServicesImp implements AccountServices {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account findAccountById(long id){
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findAccountByNumber(String number){
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account findAccountByTitularAndNumber(Client client, String number){
        return accountRepository.findByTitularAndNumber(client, number);
    }

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

}
