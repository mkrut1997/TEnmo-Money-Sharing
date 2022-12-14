package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/transactions")
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private UserDao userDao;

    public TransactionController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<String> findAllUsers (Principal principal) {
        List<User> users = new ArrayList<>();
        List<String> userNames = new ArrayList<>();
        users = userDao.findAll();
        for (User user: users) {
            if (!user.getUsername().equals(principal.getName())) {
              userNames.add(user.getUsername());
            }
        }
        return userNames;
    }
}


