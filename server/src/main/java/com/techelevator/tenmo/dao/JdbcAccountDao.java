package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    @Override
    public Account getAccountByUsername(String userName) {
        Account account = null;
        String sql = "SELECT account_id, account.user_id, balance FROM account\n" +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
                "WHERE username = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userName);
        if(result.next()){
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if(result.next()){
            account = mapRowToAccount(result);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }

    @Override
    public void transaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account SET balance = balance - ? " +
                "WHERE user_id = ?; " +
                "UPDATE account SET balance  = balance + ? " +
                "WHERE user_id = ?; " +
                "COMMIT;";
        jdbcTemplate.update(sql, amount, fromAccount.getUserId() ,amount, toAccount.getUserId());
    }
}
