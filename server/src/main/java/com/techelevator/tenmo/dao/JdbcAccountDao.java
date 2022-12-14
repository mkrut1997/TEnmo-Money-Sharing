package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements  AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account createNewAccount(Account account) {
        Account newAccount = null;
        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES (?, ?) RETURNING account_id;";
        int newId = jdbcTemplate.queryForObject(sql, Integer.class, account.getUserId(), account.getBalance());
        newAccount = account;
        newAccount.setAccountId(newId);
        return newAccount;
    }


}
