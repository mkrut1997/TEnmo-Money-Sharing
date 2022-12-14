package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction) {
        Transaction newTransaction = transaction;
        String sql = "INSERT INTO transaction (from_user_id, to_user_id, amount, status ) " +
                "VALUES(?, ?, ?, ?) RETURNING transaction_id;";
        int newId = jdbcTemplate.queryForObject(sql, int.class, transaction.getFromUser(), transaction.getToUser(), transaction.getAmount(), transaction.getStatus());
        newTransaction.setTransactionId(newId);
        return newTransaction;
    }

    @Override
    public List<Transaction> getAllTransactionsByUser(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM transaction " +
                "WHERE from_user_id = ? OR to_user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (result.next()) {
            transactions.add(mapToRowTransaction(result));
        }
        return transactions;
    }

    @Override
    public Transaction getTransactionById(int transactionId) {
        Transaction transaction = null;
        String sql = "SELECT * " +
                "FROM transaction " +
                "WHERE transaction_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transactionId);
        if (result.next()) {
            transaction = mapToRowTransaction(result);
        }
        return transaction;
    }

    private Transaction mapToRowTransaction (SqlRowSet rowSet) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rowSet.getInt("transaction_id"));
        transaction.setFromUser(rowSet.getInt("from_user_id"));
        transaction.setToUser(rowSet.getInt("to_user_id"));
        transaction.setAmount(rowSet.getBigDecimal("amount"));
        transaction.setStatus(rowSet.getString("status"));
        return transaction;
    }
}


