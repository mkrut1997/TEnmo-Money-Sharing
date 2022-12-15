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
    @RequestMapping( path = "/to_{toUserName}/{amount}" ,method = RequestMethod.POST)
    public Transaction transact(@PathVariable String toUserName,
                                @PathVariable BigDecimal amount,
                                Principal principal){
        Account fromAccount = accountDao.getAccountByUsername(principal.getName());
        if(fromAccount.getBalance().compareTo(amount) < 0 || amount.compareTo(new BigDecimal("0.01")) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid amount OR not enough money in account");
        }
        Account toAccount = accountDao.getAccountByUsername(toUserName);
        if(fromAccount.getUserId() == toAccount.getUserId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't send money to yourself");
        }
        Transaction transaction = new Transaction();
        transaction.setToUser(toAccount.getUserId());
        transaction.setFromUser(fromAccount.getUserId());
        transaction.setAmount(amount);
        transaction.setStatus("Approved");
        Transaction newTransaction = transactionDao.createNewTransaction(transaction);
        accountDao.transaction(fromAccount, toAccount, amount);
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
    @RequestMapping(path = "/from_{userName}/{amount}", method = RequestMethod.POST)
    public Transaction requestTransaction(@PathVariable String userName,
                                          @PathVariable BigDecimal amount,
                                          Principal principal) {
        Account fromAccount = accountDao.getAccountByUsername(userName);
        Account toAccount = accountDao.getAccountByUsername(principal.getName());
        if(amount.compareTo(new BigDecimal("0.01")) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid amount");
        }
        if(fromAccount.getUserId() == toAccount.getUserId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't request money from yourself");
        }
        Transaction transaction = new Transaction();
        transaction.setToUser(toAccount.getUserId());
        transaction.setFromUser(fromAccount.getUserId());
        transaction.setAmount(amount);
        transaction.setStatus("Pending");
        Transaction newTransaction = transactionDao.createNewTransaction(transaction);
        return newTransaction;
    }

    @RequestMapping(path = "/pendingtransactions", method = RequestMethod.GET)
    public List<Transaction> getAllPendingTransactions (Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        transactions = transactionDao.getAllPendingTransactions(accountDao.getAccountByUsername(principal.getName()).getUserId(), "Pending");
        return transactions;
    }

    @RequestMapping(path = "/{id}/{action}", method = RequestMethod.PUT)
    public  void updateStatusOfTransaction (@PathVariable int id, @PathVariable String action, Principal principal) {

    }

}


