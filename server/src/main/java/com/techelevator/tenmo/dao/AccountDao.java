package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account createNewAccount (Account account);
    Account getAccountByUsername(String userName);
    void transaction (Account fromAccount, Account toAccount, BigDecimal amount);
    Account getAccountByUserId (int userId);

}
