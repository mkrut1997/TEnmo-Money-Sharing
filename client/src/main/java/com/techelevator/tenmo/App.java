package com.techelevator.tenmo;

import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.AuthenticationService;
import com.techelevator.tenmo.service.ConsoleService;

public class App {

    private final ConsoleService consoleService = new ConsoleService();
    private final AccountService accountService = new AccountService();
    private final AuthenticationService authenticationService = new AuthenticationService();
    private String userName = null;

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();


    }

    public void run() {
        consoleService.welcomeMessage();
        handleLogin();
        consoleService.displayMessage("Welcome " + userName + "!");
        consoleService.displayMessage("Here is your current balance: " + accountService.getBalance());
        consoleService.displayOptions();
        int menuSelection = consoleService.promptForMenuSelection();
        if(menuSelection == 1){
            consoleService.displayMessage("Please select a user from the list below");

        }
    }

    private void handleLogin() {
        while(true) {
            String username = consoleService.promptForString("Username: ");
            String password = consoleService.promptForString("Password: ");
            String token = authenticationService.login(username, password);
            if (token != null) {
                accountService.setAuthToken(token);
                setUserName(username);
                break;
            } else {
                consoleService.displayMessage("Invalid Username or Password, try again");
            }
        }
    }
}


