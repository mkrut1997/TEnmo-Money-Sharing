package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    public TransactionController(UserDao userDao, AccountDao accountDao, TransactionDao transactionDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<String> findAllUsers (Principal principal) {
        List<User> users = new ArrayList<>();
        List<String> userNames = new ArrayList<>();
        users = userDao.findAll();
        for (User user: users) {
            if (!user.getUsername().equals(principal.getName())) {
              userNames.add(user.getUsername());
            }
        }
        return userNames;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping( path = "/sendBucks" ,method = RequestMethod.POST)
    public Transaction transact(@RequestBody Transaction transaction,
                                Principal principal){
        Account fromAccount = accountDao.getAccountByUsername(principal.getName());
        if(fromAccount.getBalance().compareTo(transaction.getAmount()) < 0 || transaction.getAmount().compareTo(new BigDecimal("0.01")) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid amount OR not enough money in account");
        }
        Account toAccount = accountDao.getAccountByUserId(transaction.getToUser());
        if(fromAccount.getUserId() == toAccount.getUserId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't send money to yourself");
        }
        transaction.setStatus("Approved");
        Transaction newTransaction = transactionDao.createNewTransaction(transaction);
        accountDao.transaction(fromAccount, toAccount, transaction.getAmount());
        return newTransaction;
    }

    @RequestMapping(path = "/myTransactions", method = RequestMethod.GET)
    public List<Transaction> getAllTransactionsByUser (Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        transactions = transactionDao.getAllTransactionsByUser(accountDao.getAccountByUsername(principal.getName()).getUserId());
        return transactions;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transaction getTransactionById (@PathVariable int id) {
        Transaction transaction = transactionDao.getTransactionById(id);
        return transaction;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/requestBucks", method = RequestMethod.POST)
    public Transaction requestTransaction(@RequestBody Transaction transaction,
                                          Principal principal) {
        Account fromAccount = accountDao.getAccountByUserId(transaction.getFromUser());
        Account toAccount = accountDao.getAccountByUsername(principal.getName());
        if(transaction.getAmount().compareTo(new BigDecimal("0.01")) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid amount");
        }
        if(fromAccount.getUserId() == toAccount.getUserId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't request money from yourself");
        }
        transaction.setStatus("Pending");
        Transaction newTransaction = transactionDao.createNewTransaction(transaction);
        return newTransaction;
    }

    @RequestMapping(path = "/pendingTransactions", method = RequestMethod.GET)
    public List<Transaction> getAllPendingTransactions (Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        transactions = transactionDao.getAllPendingTransactions(accountDao.getAccountByUsername(principal.getName()).getUserId(), "Pending");
        return transactions;
    }

    @RequestMapping(path = "/updatePendingTransaction", method = RequestMethod.PUT)
    public  void updateStatusOfTransaction (@RequestBody Transaction transaction, Principal principal) {

        //Must make sure principal is the one being requested FROM
        if(transaction.getFromUser() != accountDao.getAccountByUsername(principal.getName()).getUserId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This request is for a different user");
        }
        else {
            if (transaction.getStatus().equals("Approved")) {
                Account fromAccount = accountDao.getAccountByUserId(transaction.getFromUser());
                if(fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money in account");
                }
                Account toAccount = accountDao.getAccountByUserId(transaction.getToUser());
                accountDao.transaction(fromAccount, toAccount, transaction.getAmount());
                transactionDao.updateStatusOfTransaction(transaction.getTransactionId(), "Approved");
            } else if (transaction.getStatus().equals("Rejected")){
                transactionDao.updateStatusOfTransaction(transaction.getTransactionId(), "Rejected");
            } else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action is not recognized");
            }
        }

    }

}


