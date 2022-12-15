package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransactionDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDaoTests extends BaseDaoTests{

    private JdbcTransactionDao transactionDao;
    private Transaction testTransaction;
    private JdbcAccountDao accountDao;

    private static final Transaction TRANSACTION_1 = new Transaction(3003, 1001, 1002, new BigDecimal("300"), "Approved");
    private static final Transaction TRANSACTION_2 = new Transaction(3001, 1001, 1002, new BigDecimal("500.00"), "Approved");
    private static final Transaction TRANSACTION_3 = new Transaction(3002, 1002, 1001, new BigDecimal("500.00"), "Approved");

    @Before
    public void setup(){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        transactionDao = new JdbcTransactionDao(template);
        accountDao = new JdbcAccountDao(template);
        Account account = new Account(2002, 1002, new BigDecimal("500.00"));
        accountDao.createNewAccount(account);
        testTransaction = new Transaction();
        testTransaction.setFromUser(1001);
        testTransaction.setToUser(1002);
        testTransaction.setAmount(new BigDecimal("300"));
        testTransaction.setStatus("Approved");
    }

    @Test
    public void assert_created_transaction_matches_expected(){
        assertTransactionsMatch(TRANSACTION_1, transactionDao.createNewTransaction(testTransaction));
    }

    @Test
    public void assert_correct_list_of_transactions_is_returned(){
        List<Transaction> expected = new ArrayList<>();
        expected.add(TRANSACTION_2);
        expected.add(TRANSACTION_3);

        List<Transaction> actual = transactionDao.getAllTransactionsByUser(1001);
        for (int i = 0; i < expected.size(); i++) {
            assertTransactionsMatch(expected.get(i), actual.get(i));
        }

        Assert.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void assert_correct_transaction_retrieved_by_id(){
        assertTransactionsMatch(TRANSACTION_2, transactionDao.getTransactionById(3001));
        assertTransactionsMatch(TRANSACTION_3, transactionDao.getTransactionById(3002));
    }



    private void assertTransactionsMatch(Transaction expected, Transaction actual){
        Assert.assertEquals(expected.getTransactionId(), actual.getTransactionId());
        Assert.assertEquals(expected.getFromUser(), actual.getFromUser());
        Assert.assertEquals(expected.getToUser(), actual.getToUser());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }


}
