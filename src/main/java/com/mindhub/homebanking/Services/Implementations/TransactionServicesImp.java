package com.mindhub.homebanking.Services.Implementations;


import com.mindhub.homebanking.Services.TransactionServices;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServicesImp implements TransactionServices {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

}
