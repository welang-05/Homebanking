package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountServices;
import com.mindhub.homebanking.Services.LoanServices;
import com.mindhub.homebanking.Services.TransactionServices;
import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    LoanServices loanServices;

    @Autowired
    TransactionServices transactionServices;

    @Autowired
    AccountServices accountServices;


    @GetMapping("/api/loans")
    public List<LoanDTO> loans(){
        return loanServices.getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> loanApplication(
            @RequestBody LoanApplicationDTO loanApplication,
            Authentication authentication
            ){
        long id = loanApplication.getId();
        double loanAmount = loanApplication.getAmount();
        int payments = loanApplication.getPayments();
        String accountNumber= loanApplication.getAccountNumber();

        if( id==0 || loanAmount==0 || payments==0 || accountNumber.isEmpty() ){
            return new ResponseEntity<>("One or more of the parameters are empty",HttpStatus.FORBIDDEN);
        }

        if(loanServices.findLoanById(id)==null){
            return new ResponseEntity<>("The type of Loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        LoanDTO loanDTO = new LoanDTO(loanServices.findLoanById(id));

        if (loanDTO.getMaxAmount()<loanAmount*loanDTO.getInterest()){
            return new ResponseEntity<>("The amount you request is over the maximum for this type of Loan",
                    HttpStatus.FORBIDDEN);
        }

        if(!loanDTO.getPayments().contains(payments)){
            return new ResponseEntity<>("The payments you request don't belong to this type of loan",
                    HttpStatus.FORBIDDEN);
        }

        Account account = accountServices.findAccountByNumber(accountNumber);

        if(account==null){
            return new ResponseEntity<>("The destiny account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(!account.getTitular().getEmail().equals(authentication.getName())){
            return new ResponseEntity<>("The destiny account doesn't belong to the authenticated client",
                    HttpStatus.FORBIDDEN);
        }

        Set<ClientLoanDTO> currentLoans = account.getTitular().getLoanSet().stream().map(loan->new ClientLoanDTO(loan)).filter(clientloan -> clientloan.getLoanId()==id).collect(Collectors.toSet());

        double accumulatedAmount=0;
        for(ClientLoanDTO loan : currentLoans){
            accumulatedAmount += loan.getAmount();
        }

        /*Esta condición es para limitar la cantidad de préstamos de un tipo, en base a la suma de los montos, la que no debe superar el monto máximo para ese tipo de préstamo*/
        if((accumulatedAmount+loanAmount)>loanDTO.getMaxAmount()){
            return new ResponseEntity<>("You have reach the maximum amount to apply in this kind of loan", HttpStatus.FORBIDDEN);
        }

        Transaction loanCredit = new Transaction(account, TransactionType.CREDIT, loanAmount, loanDTO.getName()+
                " loan " +
                "approved",
                LocalDateTime.now());
        account.setBalance(account.getBalance()+loanAmount);

//        Double amount, int payments, Client client, Loan loan

        loanServices.saveClientLoan(new ClientLoan(loanAmount*loanDTO.getInterest(),payments,account.getTitular(),
                loanServices.findLoanById(id) ) );
        transactionServices.saveTransaction(loanCredit);
        accountServices.saveAccount(account);

        return new ResponseEntity<>("Loan approved",HttpStatus.CREATED);
    }

    @PostMapping("/api/loans/types")
    public ResponseEntity<Object> newLoanType(@RequestBody LoanDTO loanDTO, Authentication authentication){

        if(loanDTO==null){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if(loanServices.findLoanByName(loanDTO.getName())!=null){
            return new ResponseEntity<>("This loan name already exist", HttpStatus.FORBIDDEN);
        }

        Loan newLoan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getInterest() , loanDTO.getPayments());
        loanServices.saveLoan(newLoan);

        return new ResponseEntity<>("New type of Loan created",HttpStatus.CREATED);
    }

}
