public class Main{
    private static AccountService accountService;

    public static void main(String[] args) throws InterruptedException {
        accountService = new AccountService();
        runMainMenu();
    }

    private static void runMainMenu() {
        int choice;
        do {
            mainMenu();
            choice = InputUtils.readInt("Enter choice: ");
            executeChoice(choice);
        } while (choice != 5);
    }

    private static void executeChoice(int choice) {
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                accountService.viewAllAccounts();
                break;
            case 3:
                processTransactionMain();
                break;
            case 4:
                System.out.println("\n VIEW TRANSACTION HISTORY ");
                System.out.println("-".repeat(25));
                String acct = InputUtils.getValidAccountNumber("user");
                System.out.println("\n");
                Account found = accountService.findAccount(acct);
                if (found == null) {
                    System.out.println("Account not found.");
                } else {
                    System.out.println("Account: " + found.getAccountNumber() + " - " + found.getCustomer().getName());
                    System.out.println("Account Type: " + found.getAccountType());
                    System.out.printf("Current Balance: $%,.2f\n", found.getBalance());
                    System.out.println();
                    accountService.getTransactions(acct);
                }
                break;
            case 5:
                System.out.println("Exiting application...");
                break;
            default:
                System.out.println("\nInvalid choice. Please select an option between 1 and 5.\n");
        }

        if (choice != 5 && choice >= 1 && choice <= 4) {
            InputUtils.readLine("\nPress Enter to continue...");
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
        System.out.print("");
    }

    public static boolean createAccount(){
        String name;
        int age;
        String contact;
        String address;
        int customerType;
        int accountType;
        double initialDeposit;

        System.out.println("ACCOUNT CREATION");
        System.out.println("-".repeat(65));
        System.out.println("\n");

        // Customer name input
        while (true) {
            name = InputUtils.readLine("Enter customer name: ");
            final String NAME_REGEX = "^[A-Za-z][A-Za-z'-]*(?:\\s[A-Za-z][A-Za-z'-]*)+$";

            if (name.matches(NAME_REGEX)) {
                break;
            } else {
                System.out.println("Invalid name format: FirstName, SurName and third name if applicable");
            }
        }

        age = InputUtils.readInt("Enter customer age: ");

        while (true) {
            contact = InputUtils.readLine("Enter customer contact: ");
            
            final String CONTACT_REGEX =
                "^(?:"
                + "0(?:24|54|55|59|25|20|50|26|56|57|23)[\\s-]?\\d{3}[\\s-]?\\d{4}"
                + "|(?:\\+233|233|00233)(?:24|54|55|59|25|20|50|26|56|57|23)[\\s-]?\\d{3}[\\s-]?\\d{4}" 
                + ")$";

            if (contact.matches(CONTACT_REGEX)) {
                break; 
            } else {
                System.out.println("Invalid contact format. Examples: 0241234567, +233241234567, 024-123-4567");
            }
        }

        while (true) {
            address = InputUtils.readLine("Enter customer address: ");
            final String ADDRESS_REGEX = "^[A-Za-z0-9][A-Za-z0-9\\s,.'\\-/#]{4,99}$";
            if (address.matches(ADDRESS_REGEX)) {
                break;
            } else {
                System.out.println("Enter valid address format (eg: 123 Oak Street, Springfield)");
            }
        }
        System.out.println("\n");

        System.out.println("Customer type: ");
        System.out.println("1. Regular Customer (Standard banking services");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance %10,000");
        customerType = InputUtils.readInt("Select type (1-2): ");
        while (customerType != 1 &&  customerType != 2) {
            System.out.println("Invalid customer type");
            System.out.println("\n");
            customerType = InputUtils.readInt("Select type (1-2): ");
            System.out.println("\n");
        }

        System.out.println("Account type: ");
        System.out.println("1. Savings Account (Interest: 3.5% Min Balance: %500");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10");
        accountType = InputUtils.readInt("Select Type: (1-2): ");
        System.out.println("\n");
        while (accountType != 1 &&  accountType != 2) {
            System.out.println("Invalid customer type");
            System.out.println("\n");
            accountType = InputUtils.readInt("Select type (1-2): ");
            System.out.println("\n");
        }

        // Read initial deposit with validation - loop until valid
        while (true) {
            try {
                String d = InputUtils.readLine("Enter initial deposit: $");
                initialDeposit = Double.parseDouble(d);
                
                if (initialDeposit <= 0) {
                    System.out.println("Initial deposit must be positive. Please try again.");
                    continue;
                }
                
                // Check minimum balance for savings account
                if (accountType == 1 && initialDeposit < 500.00) {
                    System.out.println("Savings account requires minimum initial deposit of $500.00. Please try again.");
                    continue;
                }
                
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        Customer customer = null;
        switch (customerType) {
            case 1:
                customer = new RegularCustomer(name, age, contact, address);
                break;
            case 2:
                customer = new PremiumCustomer(name, age, contact, address);
                break;
            default:
                System.out.println("Invalid Customer type selected. Aborting account creation.");
                return false;
        }

        Account newAccount = null;
        switch (accountType) {
            case 1:
                newAccount = new SavingsAccount(customer, initialDeposit);
                break;
            case 2:
                newAccount = new CheckingAccount(customer, initialDeposit);
        }

        newAccount.displayAccountDetails();
        accountService.addAccount(newAccount);
        System.out.println("\n");

        return true;
    }

    public static boolean processTransactionMain(){
        String accountNumber = "";
        String transactionType = "";
        String recipientAccountNumber = "";
        double amount = 0.00;

        System.out.println("\nPROCESS TRANSACTION");
        System.out.println("-".repeat(63));
        System.out.println("\n");

        accountNumber = InputUtils.getValidAccountNumber("user");

        Account account = accountService.findAccount(accountNumber);
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
        System.out.println("3. Transfer");
        System.out.println("\n");
        int transactionTypeInput = InputUtils.readInt("Select type (1-3): ");
        while (transactionTypeInput != 1 && transactionTypeInput != 2 && transactionTypeInput != 3) {
            System.out.println("Invalid transaction type");
            System.out.println("\n");
            transactionTypeInput = InputUtils.readInt("Select type (1-3): ");
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
            recipientAccountNumber = InputUtils.getValidAccountNumber("Transfer");
        }

        // Read amount with validation - LOOP UNTIL VALID (NO ABORT)
        while (true) {
            try {
                String s = InputUtils.readLine("Enter amount: $");
                amount = Double.parseDouble(s);

                // Validate positive amount
                if (amount <= 0) {
                    System.out.println("Amount must be positive. Please try again.");
                    continue;
                }

                // Calculate new balance for validation
                double newBalance;
                if (transactionType.equalsIgnoreCase("Deposit")) {
                    newBalance = previousBalance + amount;
                } else {
                    newBalance = previousBalance - amount;
                }

                // Validate based on account type
                boolean validAmount = true;
                if (transactionType.equalsIgnoreCase("Withdrawal") || transactionType.equalsIgnoreCase("Transfer")) {
                    if (account instanceof SavingsAccount sa) {
                        if (newBalance < sa.getMinimumBalance()) {
                            System.out.printf("Withdrawal would violate minimum balance ($%.2f). Please enter a smaller amount.\n", 
                                sa.getMinimumBalance());
                            validAmount = false;
                        }
                    } else if (account instanceof CheckingAccount ca) {
                        if (newBalance < -ca.getOverDraftLimit()) {
                            System.out.printf("Withdrawal would exceed overdraft limit ($%.2f). Please enter a smaller amount.\n", 
                                ca.getOverDraftLimit());
                            validAmount = false;
                        }
                    }
                }

                if (validAmount) {
                    break; // Exit loop if amount is valid
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        // Calculate final new balance for confirmation
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
        String confirm = InputUtils.readLine("Confirm transaction? (Y/N): ").trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean ok = false;
            if (transactionType.equalsIgnoreCase("Transfer")) {
                ok = accountService.transferMoney(accountNumber, recipientAccountNumber, amount);
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