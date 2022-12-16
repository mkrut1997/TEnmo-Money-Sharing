package com.techelevator.tenmo.service;

import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public void welcomeMessage() {
        System.out.println("Welcome to Tenmo Application!");
        System.out.println("Please login.");
    }

    public String promptForString(String message){
        System.out.print(message);
        return scanner.nextLine();
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public void displayOptions(){
        System.out.println("What can we do for you?");
        System.out.println("1) Send Bucks\n" +
                           "2) Request Bucks\n" +
                           "3) View all transactions\n" +
                           "4) View pending transfers\n" +
                           "5) Exit");
    }

    public int promptForMenuSelection() {
        int menuSelection;
        System.out.print("Please choose an option: ");
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }



}
