package com.miracle;
// Import all necessary packages
import com.miracle.src.handlers.AccountCreationHandler;
import com.miracle.src.handlers.TransactionHandler;
import com.miracle.src.models.exceptions.AccountNotFoundException;
import com.miracle.src.models.exceptions.InvalidAmountException;
import com.miracle.src.models.exceptions.OverdraftExceededException;
import com.miracle.src.services.*;
import com.miracle.src.utils.InputUtils;
import com.miracle.src.utils.ValidationUtils;
import com.miracle.src.services.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;


public class Main {
//    private static AccountService accountService;
    private static final AccountManager accountManager = AccountManager.getInstance();
    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final AccountCreationHandler accountCreationHandler = new AccountCreationHandler();
    private static final TransactionHandler transactionHandler = new TransactionHandler();



    public static void main(String[] args) throws InterruptedException, InvalidAmountException, OverdraftExceededException {
//        accountService = new AccountService();
        runMainMenu();
    }

    private static void runMainMenu() throws InvalidAmountException, OverdraftExceededException {
        int choice;
        do {
            mainMenu();
            choice = InputUtils.readInt("Enter choice: ");
            executeChoice(choice);
        } while (choice != 5);
    }

    private static void executeChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    manageAccounts();
                    break;

                case 2:
                    transactionHandler.handleTransaction();
                    break;

                case 3:
                    generateReports();
                    break;

                case 4:
                    // Manage transaction history (menu for all / by account / account details)

                    break;

                case 5:
                    // Display all customers
                    accountManager.displayAllCustomers();
                    break;

                case 6:
                    System.out.println("Exiting application...");
                    return; // exit immediately

                default:
                    System.out.println("\nInvalid choice. Please select an option between 1 and 6.\n");
            }

        } catch (InvalidAmountException | OverdraftExceededException e) {
            System.out.println("\nERROR: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println("\nERROR: Account not found.");
        }

        // Pause before returning to menu if not exiting
        if (choice != 6) {
            InputUtils.readLine("\nPress Enter to continue... ");
        }
    }



    public static void mainMenu(){
        System.out.println("\n\n"+"=".repeat(65));
        System.out.println("||                   BANK ACCOUNT MANAGEMENT                  ||");
        System.out.println("=".repeat(65));
        System.out.println("1. Manage Accounts");
        System.out.println("2. Perform Transactions");
        System.out.println("3. Generate Statements");
        System.out.println("4. Run tests");
        System.out.println("5. Exit");
        System.out.println("\n");
    }


    public static void generateReports() {
        System.out.println("\n==== TRANSACTION HISTORY MENU ====");
        System.out.println("1. View ALL Transactions");
        System.out.println("2. View Transactions By Account");
        System.out.println("3. View Account Details");
        System.out.println("0. Back to Main Menu");

        int choice = InputUtils.readInt("Enter choice: ");

        switch (choice) {

            case 1:
                StatementGenerator.displayAllTransactions();
                break;

            case 2:
                StatementGenerator.requestAndGenerateStatement();   // This already throws/catches exceptions
                break;

            case 3:
                String accNum = ValidationUtils.getValidAccountNumber("Enter Account Number: ");
                StatementGenerator.displayAccountDetail(accNum);  // This catches AccountNotFoundException
                break;

            case 0:
                System.out.println("Returning to main menu...");
                return;

            default:
                System.out.println("Invalid selection. Please try again.");
        }
    }


    public static void manageAccounts() throws InvalidAmountException, OverdraftExceededException {
        while (true) {
            System.out.println("\n\n"+"-".repeat(65));
            System.out.println("||                   MANAGE ACCOUNTS                  ||");
            System.out.println("-".repeat(65));
            System.out.println("\n1. Create Account");
            System.out.println("2. View Accounts");
            System.out.println("3. Display All customers");

            int choice = InputUtils.readInt("Select option (1-3): ");
            System.out.println("\n");

            if (choice == 1) {
                accountCreationHandler.handleCreateAccount();
                return; // exit method completely
            } else if (choice == 2) {
                accountManager.viewAllAccounts();
                return; // exit method completely
            } else if (choice == 3) {
                accountManager.displayAllCustomers();
                return; // exit method completely
            } else {
                System.out.println("Invalid input! Please enter a number between 1 and 3.\n");
            }
        }
    }



}