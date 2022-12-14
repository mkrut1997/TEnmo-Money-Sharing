package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

public interface TransactionDao {

    Transaction createNewTransaction (Transaction transaction);

}
