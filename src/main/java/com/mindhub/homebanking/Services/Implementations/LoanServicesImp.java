package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.LoanServices;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repository.ClientLoanRepository;
import com.mindhub.homebanking.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServicesImp implements LoanServices {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public List<Loan> getAllLoans(){
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanByName(String name){
        return loanRepository.findByName(name);
    }

    @Override
    public Loan findLoanById(long id){
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveLoan(Loan loan){
        loanRepository.save(loan);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan){
        clientLoanRepository.save(clientLoan);
    }

}
