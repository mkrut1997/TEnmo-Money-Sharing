package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction) {
        Transaction newTransaction = transaction;
        String sql = "INSERT INTO transaction (from_user_id, to_user_id, amount) " +
                "VALUES(?, ?, ?) RETURNING transaction_id;";
        int newId = jdbcTemplate.queryForObject(sql, int.class, transaction.getFromUser(), transaction.getToUser(), transaction.getAmount());
        newTransaction.setTransactionId(newId);
        return newTransaction;
    }
}
