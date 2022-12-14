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
    public void createNewTransaction(Transaction transaction) {
        String sql = "INSERT INTO transcation (from_user_id, to_user_id, amount) " +
                "VALUES(?, ?, ?);";
        jdbcTemplate.update(sql, transaction.getFromUser(), transaction.getToUser(), transaction.getAmount());

    }
}
