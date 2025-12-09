package src.services;

import src.dto.AccountRequest;
import src.dto.TransactionRequest;
import src.models.*;
import src.models.exceptions.InvalidAmountException;
import src.models.exceptions.OverdraftExceededException;

public class AccountManager {

    // Singleton instance
    private static final AccountManager INSTANCE = new AccountManager();

    public static AccountManager getInstance() {
        return INSTANCE;
    }

    private Account[] accounts = new Account[50];

    private AccountManager() {
    }

    private int accountCount = 0;


    public void addAccount(Account account) {
        if (accountCount < accounts.length) {
            accounts[accountCount] = account;
            accountCount++;
        }
    }


    //    linear search through the Accounts array to find an account using
//    the account number
    public Account findAccount(String accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equalsIgnoreCase(accountNumber)) {
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
                        SavingsAccount.getMinimumBalance()
                );

            } else if (account instanceof CheckingAccount checkingAccount) {
                System.out.printf("| %-8s | Overdraft Limit: $%,.2f | Monthly Fee: $%,.2f |\n",
                        "",
                        checkingAccount.getOverDraftLimit(),

                        account.getCustomer().getCustomerType() == "Premium" ? 0.00 : checkingAccount.getMonthlyFee()
                );
            }
            System.out.println("-".repeat(83));
        }

        // Display required totals
        System.out.printf("\nTotal Accounts: %d\n", getAccountCount()); //
        System.out.printf("Total Bank Balance: $%,.2f\n", getTotalBalance()); //
    }


    //    Get all the money available at the bank.
    public double getTotalBalance() {
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


    public void createAccount(AccountRequest req) throws InvalidAmountException {

//        Creating the customer based on the account type selected
        Customer customer = null;
        if (req.getCustomerType() == 1) {
            customer = new RegularCustomer(req.getName(), req.getAge(), req.getContact(), req.getAddress());
        } else {
            customer = new PremiumCustomer(req.getName(), req.getAge(), req.getContact(), req.getAddress());
        }

//        Creating the account based on the type selected
        Account account;
        if (req.getAccountType() == 1) {
            account = new SavingsAccount(customer, req.getInitialDeposit());
        } else {
            account = new CheckingAccount(customer, req.getInitialDeposit());
        }
        account.displayAccountDetails();
        addAccount(account);
    }


    public void processTransaction(TransactionRequest req) throws InvalidAmountException, OverdraftExceededException {
        Account userAccount = findAccount(req.getUserAccountNumber());
        Account receiverAccount = findAccount(req.getReceiverAccountNumber());

        if (req.getTransactionType().equalsIgnoreCase("Transfer"))
            receiverAccount.processTransaction(req.getAmount(), "Receive");
        userAccount.processTransaction(req.getAmount(), req.getTransactionType());
    }



    public void displayAllCustomers() {
        if (accountCount == 0) {
            System.out.println("\nNo customers found.");
            return;
        }

        System.out.println("\nALL CUSTOMERS");
        System.out.println("=".repeat(85));
        System.out.printf("| %-6s | %-25s | %-5s | %-15s | %-25s |\n",
                "ID", "NAME", "AGE", "CONTACT", "ADDRESS");
        System.out.println("-".repeat(85));

        for (int i = 0; i < accountCount; i++) {
            Customer c = accounts[i].getCustomer();

            System.out.printf("| %-6s | %-25s | %-5d | %-15s | %-25s |\n",
                    c.getCustomerId(),   // if you have a customer ID
                    c.getName(),
                    c.getAge(),
                    c.getContact(),
                    c.getAddress()
            );
        }

        System.out.println("=".repeat(85));
        System.out.printf("Total Customers: %d\n", accountCount);
    }

}
