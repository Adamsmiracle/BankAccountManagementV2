public class AccountService {
    private final AccountManager accountManager = AccountManager.getInstance();
    private final TransactionManager transactionManager = TransactionManager.getInstance();

    public void addAccount(Account account) {
        accountManager.addAccount(account);
    }

    public Account findAccount(String accountNumber) {
        return accountManager.findAccount(accountNumber);
    }

    public void viewAllAccounts() {
        accountManager.viewAllAccounts();
    }

    public void getTransactions(String accountNumber) {
         transactionManager.viewTransactionsByAccount(accountNumber);
    }

    public boolean processTransactionService(Account account, double amount, String type) {
        if (type.equalsIgnoreCase("Deposit")) {
            Transaction t = account.deposit(amount);
            return t != null;
        } else if (type.equalsIgnoreCase("Withdrawal")) {
            Transaction t = account.withdraw(amount);
            return t != null;
        }
        return false;
    }

    /**
     * Transfer money between two accounts with comprehensive validation
     * @param senderAccountNumber - Source account number
     * @param recipientAccountNumber - Destination account number
     * @param amount - Amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferMoney(String senderAccountNumber, String recipientAccountNumber, double amount) {
        // 1. VERIFY BOTH ACCOUNTS EXIST
        Account sender = accountManager.findAccount(senderAccountNumber);
        if (sender == null) {
            System.out.println("Error: Sender account not found.");
            return false;
        }

        Account recipient = accountManager.findAccount(recipientAccountNumber);
        if (recipient == null) {
            System.out.println("Error: Recipient account not found.");
            return false;
        }

        // 2. PREVENT TRANSFER TO SAME ACCOUNT
        if (senderAccountNumber.equalsIgnoreCase(recipientAccountNumber)) {
            System.out.println("Error: Cannot transfer to the same account.");
            return false;
        }

        // 3. ENSURE SUFFICIENT BALANCE IN SENDER ACCOUNT
        // Check if withdrawal would violate account rules
        double senderNewBalance = sender.getBalance() - amount;
        
        if (sender instanceof SavingsAccount) {
            SavingsAccount sa = (SavingsAccount) sender;
            if (senderNewBalance < sa.getMinimumBalance()) {
                System.out.printf("Error: Transfer would violate sender's minimum balance requirement ($%.2f).\n", 
                    sa.getMinimumBalance());
                return false;
            }
        } else if (sender instanceof CheckingAccount) {
            CheckingAccount ca = (CheckingAccount) sender;
            if (senderNewBalance < -ca.getOverDraftLimit()) {
                System.out.printf("Error: Transfer would exceed sender's overdraft limit ($%.2f).\n", 
                    ca.getOverDraftLimit());
                return false;
            }
        }

        // 4. PROCESS TRANSFER WITH PROPER TRANSACTION TYPES
        // Deduct from sender as "Transfer Out"
        boolean senderSuccess = sender.processTransaction(amount, "Transfer Out");
        
        if (!senderSuccess) {
            System.out.println("Error: Failed to deduct amount from sender account.");
            return false;
        }

        // Add to recipient as "Transfer In"
        boolean recipientSuccess = recipient.processTransaction(amount, "Transfer In");
        
        if (!recipientSuccess) {
            // Rollback: Add the amount back to sender if recipient fails
            sender.processTransaction(amount, "Deposit");
            System.out.println("Error: Failed to credit recipient account. Transaction rolled back.");
            return false;
        }

        // 5. SUCCESS
        System.out.printf("\nTransfer successful!\n");
        System.out.printf("From: %s (%s) - New Balance: $%,.2f\n", 
            sender.getAccountNumber(), 
            sender.getCustomer().getName(),
            sender.getBalance());
        System.out.printf("To: %s (%s) - New Balance: $%,.2f\n", 
            recipient.getAccountNumber(), 
            recipient.getCustomer().getName(),
            recipient.getBalance());

        return true;
    }
}