package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{


    private JdbcAccountDao accountDao;
    private Account testAccount;

    private static final Account ACCOUNT_1 = new Account(2002, 1002, new BigDecimal("500.00"));
    private static final Account ACCOUNT_2 = new Account(2001, 1001, new BigDecimal("1000.00"));

    @Before
    public void setup(){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        accountDao = new JdbcAccountDao(template);
        testAccount = new Account();
        testAccount.setUserId(1002);
        testAccount.setBalance(new BigDecimal("500.00"));
    }

    @Test
    public void assert_create_account_matches_expected_in_database(){
        Account account = accountDao.createNewAccount(testAccount);
        assertAccountsMatch(ACCOUNT_1, account);
    }

    @Test
    public void assert_find_account_by_username_returns_correct_account(){
        assertAccountsMatch(ACCOUNT_2, accountDao.getAccountByUsername("bob"));
    }

    //transaction method had to be tested manually in Postman and database



    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

}
