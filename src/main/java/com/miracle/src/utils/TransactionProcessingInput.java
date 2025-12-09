package src.utils;

import src.dto.TransactionRequest;
import src.models.Account;
import src.models.Transaction;
import src.services.AccountManager;

import javax.xml.transform.Source;

public class TransactionProcessingInput {

    static AccountManager accountManager = AccountManager.getInstance();

    public TransactionProcessingInput() {
    }

    public static TransactionRequest processTransactionMain() {
        String transactionType = "";
        String recipientAccountNumber = "";
        double amount = 0.00;

        System.out.println("\nPROCESS TRANSACTION");
        System.out.println("=".repeat(63));
        System.out.println("\n");

        // Taking user's account number
        String accountNumber = "";

        // Get sender's account
        Account account;
        while (true) {
            accountNumber = ValidationUtils.getValidAccountNumber("user");

            if (accountNumber == null || accountNumber.equalsIgnoreCase("exit")) {
                System.out.println("Exiting transaction process...");
                return null;  // exit fully from the function
            }

            account = accountManager.findAccount(accountNumber);

            if (account == null) {
                System.out.println("Account not found. Please try again!");
                continue;
            }
            break;  // account found
        }

        // Display account details
        System.out.println("ACCOUNT DETAILS");
        System.out.println("Customer: " + account.getCustomer().getName());
        System.out.println("Account Type: " + account.getAccountType());
        double previousBalance = account.getBalance();
        System.out.printf("Current Balance: $%,.2f\n", previousBalance);
        System.out.println("\n");

        // Transaction type selection
        System.out.println("Transaction type: ");
        System.out.println("1. Deposit");
        System.out.println("2. Withdrawal");
        System.out.println("3. Transfer");
        System.out.println("\n");
        int transactionTypeInput = InputUtils.readInt("Select type (1-3): ");
        while (transactionTypeInput < 1 || transactionTypeInput > 3) {
            System.out.println("Invalid transaction type");
            transactionTypeInput = InputUtils.readInt("Select type (1-3): ");
        }

        if (transactionTypeInput == 1) transactionType = "Deposit";
        else if (transactionTypeInput == 2) transactionType = "Withdrawal";
        else transactionType = "Transfer";

        // Get recipient account for transfers
        if (transactionType.equalsIgnoreCase("Transfer")) {
            Account recipientAccount;
            while (true) {
                recipientAccountNumber = ValidationUtils.getValidAccountNumber("transfer");

                if (recipientAccountNumber == null || recipientAccountNumber.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting transaction process...");
                    return null;  // fully exit
                }

                recipientAccount = accountManager.findAccount(recipientAccountNumber);

                if (recipientAccount == null) {
                    System.out.println("Recipient account not found. Please try again!");
                    continue;
                }
                break;  // recipient found
            }
        }

        // Get transaction amount
        amount = ValidationUtils.getValidAmount("Enter Amount: ");

        // Compute new balance (simple)
        double newBalance = transactionType.equalsIgnoreCase("Deposit") ?
                previousBalance + amount :
                previousBalance - amount;

        // Confirmation
        System.out.println("\nTRANSACTION CONFIRMATION");
        System.out.println("-".repeat(63));
        System.out.println("Transaction ID: " + Transaction.getNextTransactionId());
        System.out.println("Account: " + accountNumber);
        System.out.println("Type: " + transactionType.toUpperCase());
        System.out.printf("Amount: $%,.2f\n", amount);
        System.out.printf("Previous Balance: $%,.2f\n", previousBalance);
        System.out.printf("New Balance: $%,.2f\n", newBalance);
        System.out.println("-".repeat(63));

        Boolean confirmTransaction = InputUtils.readYesNo("Confirm transaction? (Y/N): ");
        if (!confirmTransaction) {
            System.out.println("Transaction cancelled by user.");
            return null; // exit fully
        }

        return new TransactionRequest(accountNumber, recipientAccountNumber, transactionType, amount);
    }

}
