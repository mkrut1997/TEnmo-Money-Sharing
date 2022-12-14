package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.util.List;

public interface TransactionDao {

    Transaction createNewTransaction (Transaction transaction);
    List<Transaction> getAllTransactionsByUser (int userId);
    Transaction getTransactionById (int transactionId);

}
