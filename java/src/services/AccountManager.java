package src.services;

import src.models.*;
import src.utils.ValidationUtils;

public class AccountManager {

    // Singleton instance
    private static final AccountManager INSTANCE = new AccountManager();

    public static AccountManager getInstance() {
        return INSTANCE;
    }

    private  Account[] accounts = new Account[50];
    
    private AccountManager() {
    }
    private int accountCount = 0;


//    Method to add an account to the accounts array
    public void addAccount(Account account){
        if (accountCount < accounts.length){
            accounts[accountCount] = account;
            accountCount++;
        }
    }


//    linear search through the Accounts array to find an account using
//    the account number
    public Account findAccount(String accountNumber){
        for(int i = 0; i < accountCount; i++){
            if (accounts[i].getAccountNumber().equalsIgnoreCase(accountNumber)){
                return accounts[i];
            }
        }
        return null;
    }

//    Get all opened accounts in the banks
    public void viewAllAccounts() {
        System.out.println("\n ACCOUNT LISTING ");
        System.out.println("-".repeat(83));
        System.out.printf("| %-8s | %-25s | %-12s | %-14s | %-8s |\n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("-".repeat(83));

        // Iterate only up to the actual number of accounts stored
        for (int i = 0; i < accountCount; i++) {
            Account account = accounts[i];

            // Line 1: Main Account Details
            System.out.printf("| %-8s | %-25s | %-12s | $%,-13.2f | %-8s |\n",
                    account.getAccountNumber(),
                    account.getCustomer().getName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus()
            );
//            Line two of the output formatter
            if (account instanceof SavingsAccount savingsAccount) {
                System.out.printf("| %-8s | Interest Rate: %.1f%% | Min Balance: $%,.2f |\n",
                        "",
                        savingsAccount.getInterestRate() * 100,
                        savingsAccount.getMinimumBalance()
                );

            } else if (account instanceof CheckingAccount checkingAccount) {
                System.out.printf("| %-8s | Overdraft Limit: $%,.2f | Monthly Fee: $%,.2f |\n",
                        "",
                        checkingAccount.getOverDraftLimit(),

                        account.getCustomer().getCustomerType() == "Premium"? 0.00: checkingAccount.getMonthlyFee()
                );
            }
            System.out.println("-".repeat(83));
        }

        // Display required totals
        System.out.printf("\nTotal Accounts: %d\n", getAccountCount()); //
        System.out.printf("Total Bank Balance: $%,.2f\n", getTotalBalance()); //
    }


//    Get all the money available at the bank.
    public double getTotalBalance(){
        double sum = 0.0;
        for (int i = 0; i < accountCount; i++) {
            sum += accounts[i].getBalance();
        }
        return sum;
    }

//    Get the number of accounts opened at the bank.
    public int getAccountCount() {
        return accountCount;
    }




    public boolean createAccount(){
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
            name = ValidationUtils.readLine("Enter customer name: ");
            final String NAME_REGEX = "^[A-Za-z][A-Za-z'-]*(?:\\s[A-Za-z][A-Za-z'-]*)+$";

            if (name.matches(NAME_REGEX)) {
                break;
            } else {
                System.out.println("Invalid name format: FirstName, SurName and third name if applicable");
            }
        }

        age = ValidationUtils.readInt("Enter customer age: ");

        while (true) {
            contact = ValidationUtils.readLine("Enter customer contact: ");

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
            address = ValidationUtils.readLine("Enter customer address: ");
            final String ADDRESS_REGEX = "^[A-Za-z0-9][A-Za-z0-9\\s,.'\\-/#]{4,99}$";
            if (address.matches(ADDRESS_REGEX)) {
                break;
            } else {
                System.out.println("Enter valid address format (eg: 123 Oak Street, Springfield)");
            }
        }
        System.out.println("\n");

        System.out.println("Customer type: ");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000)");
        customerType = ValidationUtils.readInt("Select type (1-2): ");
        while (customerType != 1 &&  customerType != 2) {
            System.out.println("Invalid customer type");
            System.out.println("\n");
            customerType = ValidationUtils.readInt("Select type (1-2): ");
            System.out.println("\n");
        }

        System.out.println("Account type: ");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        accountType = ValidationUtils.readInt("Select Type (1-2): ");
        System.out.println("\n");
        while (accountType != 1 &&  accountType != 2) {
            System.out.println("Invalid account type");
            System.out.println("\n");
            accountType = ValidationUtils.readInt("Select type (1-2): ");
            System.out.println("\n");
        }

        // Read initial deposit with validation - loop until valid
        while (true) {
            try {
                String d = ValidationUtils.readLine("Enter initial deposit: $");
                initialDeposit = Double.parseDouble(d);

                if (initialDeposit <= 0) {
                    System.out.println("Initial deposit must be positive. Please try again.");
                    continue;
                }

                if (accountType == 1 && initialDeposit < 500.00) {
                    System.out.println("Savings account requires minimum initial deposit of $500.00. Please try again.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        // Create the customer
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

        // Create the account
        Account newAccount = null;
        switch (accountType) {
            case 1: newAccount = new SavingsAccount(customer, initialDeposit);
                break;
            case 2: newAccount = new CheckingAccount(customer, initialDeposit);
                break;
        }

        newAccount.displayAccountDetails();
        addAccount(newAccount);
        System.out.println("\n");

        return true;
    }
}
