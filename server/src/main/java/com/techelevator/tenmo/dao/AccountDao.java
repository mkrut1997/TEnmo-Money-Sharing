package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    Account createNewAccount (Account account);
    Account getAccountByUsername(String userName);

}
