package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanServices {

    List<Loan> getAllLoans();

    Loan findLoanByName(String name);

    Loan findLoanById(long id);

    void saveLoan(Loan loan);

    void saveClientLoan(ClientLoan clientLoan);
}
