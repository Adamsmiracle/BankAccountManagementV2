// NO package declaration for Main.java since it's in the root

// Import all necessary packages
import src.models.*;
import src.services.*;
import src.utils.*;


public class Main {
    private static AccountService accountService;
    private static AccountManager accountManager = AccountManager.getInstance();
    private static TransactionManager transactionManager = TransactionManager.getInstance();



    public static void main(String[] args) throws InterruptedException {
        accountService = new AccountService();
        runMainMenu();
    }

    private static void runMainMenu() {
        int choice;
        do {
            mainMenu();
            choice = ValidationUtils.readInt("Enter choice: ");
            executeChoice(choice);
        } while (choice != 5);
    }

    private static void executeChoice(int choice) {
        switch (choice) {
            case 1:
                 accountManager.createAccount();
                break;
            case 2:
                accountManager.viewAllAccounts();
                break;
            case 3:
                processTransactionMain();
                break;
            case 4:
                System.out.println("\n VIEW TRANSACTION HISTORY ");
                System.out.println("-".repeat(25));
                String acct = ValidationUtils.getValidAccountNumber("user");
                System.out.println("\n");
                Account found = transactionManager.viewTransactionsByAccount(acct);
                if (found == null) {
                    System.out.println("Account not found.");
                } else {
//                    System.out.println("Account: " + found.getAccountNumber() + " - " + found.getCustomer().getName());
                    System.out.println("Account Type: " + found.getAccountType());
                    System.out.printf("Current Balance: $%,.2f\n", found.getBalance());
                    System.out.println();
                    ;
                }
                break;
            case 5:
                System.out.println("Exiting application...");
                break;
            default:
                System.out.println("\nInvalid choice. Please select an option between 1 and 5.\n");
        }

        if (choice != 5 && choice >= 1 && choice <= 4) {
            ValidationUtils.readLine("\nPress Enter to continue...");
        }
    }

    public static void mainMenu(){
        System.out.println("\n\n"+"-".repeat(65));
        System.out.println("||                   BANK ACCOUNT MANAGEMENT                  ||");
        System.out.println("-".repeat(65));
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Process Transaction");
        System.out.println("4. View Transactions History");
        System.out.println("5. Exit");
        System.out.println("\n");
    }





    public static boolean processTransactionMain(){
        String accountNumber = "";
        String transactionType = "";
        String recipientAccountNumber = "";
        double amount = 0.00;

        System.out.println("\nPROCESS TRANSACTION");
        System.out.println("-".repeat(63));
        System.out.println("\n");

        accountNumber = ValidationUtils.getValidAccountNumber("user");

        Account account = accountManager.findAccount(accountNumber);
        System.out.println("\n");
        if (account == null) {
            System.out.println("Account not found. Aborting transaction.");
            return false;
        }

        System.out.println("Account Details:");
        System.out.println("Customer: "+ account.getCustomer().getName());
        System.out.println("Account Type: "+ account.getAccountType());
        double previousBalance = account.getBalance();
        System.out.printf("Current Balance: $%,.2f\n", previousBalance);
        System.out.println("\n");

        System.out.println("Transaction type: ");
        System.out.println("1. Deposit");
        System.out.println("2. Withdrawal");
//        System.out.println("3. Transfer");
        System.out.println("\n");
        int transactionTypeInput = ValidationUtils.readInt("Select type (1-3): ");
        while (transactionTypeInput != 1 && transactionTypeInput != 2 && transactionTypeInput != 3) {
            System.out.println("Invalid transaction type");
            System.out.println("\n");
            transactionTypeInput = ValidationUtils.readInt("Select type (1-3): ");
            System.out.println("\n");
        }

        if (transactionTypeInput == 1){
            transactionType = "Deposit";
        } else if(transactionTypeInput == 2) {
            transactionType = "Withdrawal";
        } else {
            transactionType = "Transfer";
        }

        // Get recipient account for transfers
        if (transactionType.equalsIgnoreCase("Transfer")) {
            recipientAccountNumber = ValidationUtils.getValidAccountNumber("Transfer");
        }

        // Read amount with validation - LOOP UNTIL VALID
        while (true) {
            try {
                String s = ValidationUtils.readLine("Enter amount: $");
                amount = Double.parseDouble(s);

                if (amount <= 0) {
                    System.out.println("Amount must be positive. Please try again.");
                    continue;
                }

                double newBalance;
                if (transactionType.equalsIgnoreCase("Deposit")) {
                    newBalance = previousBalance + amount;
                } else {
                    newBalance = previousBalance - amount;
                }

                boolean validAmount = true;
                if (transactionType.equalsIgnoreCase("Withdrawal") || transactionType.equalsIgnoreCase("Transfer")) {
                    if (account instanceof SavingsAccount) {
                        SavingsAccount sa = (SavingsAccount) account;
                        if (newBalance < sa.getMinimumBalance()) {
                            System.out.printf("Withdrawal would violate minimum balance ($%.2f). Please enter a smaller amount.\n",
                                    sa.getMinimumBalance());
                            validAmount = false;
                        }
                    } else if (account instanceof CheckingAccount) {
                        CheckingAccount ca = (CheckingAccount) account;
                        if (newBalance < -ca.getOverDraftLimit()) {
                            System.out.printf("Withdrawal would exceed overdraft limit ($%.2f). Please enter a smaller amount.\n",
                                    ca.getOverDraftLimit());
                            validAmount = false;
                        }
                    }
                }

                if (validAmount) {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        double newBalance;
        if (transactionType.equalsIgnoreCase("Deposit")) {
            newBalance = previousBalance + amount;
        } else {
            newBalance = previousBalance - amount;
        }

        System.out.println("\n");
        System.out.println("TRANSACTION CONFIRMATION");
        System.out.println("-".repeat(63));
        System.out.println("Transaction ID: "+ Transaction.getNextTransactionId());
        System.out.println("Account: " + accountNumber);
        System.out.println("Type: " + transactionType.toUpperCase());
        System.out.printf("Amount: $%,.2f\n", amount);
        System.out.printf("Previous Balance: $%,.2f\n", previousBalance);
        System.out.printf("New Balance: $%,.2f\n", newBalance);
        System.out.println("-".repeat(63));
        String confirm = ValidationUtils.readLine("Confirm transaction? (Y/N): ").trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean ok = false;
            if (transactionType.equalsIgnoreCase("Transfer")) {
//                ok = accountService.transferMoney(accountNumber, recipientAccountNumber, amount);
            } else {
                ok = accountService.processTransactionService(account, amount, transactionType);
            }
            if (ok) {
                System.out.println("\u2713 Transaction completed successfully!\n");
            } else {
                System.out.println("Transaction failed during processing.");
            }
        } else {
            System.out.println("Transaction cancelled.");
        }

        return true;
    }
}