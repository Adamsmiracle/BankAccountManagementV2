import java.util.Arrays;
import java.util.Comparator;

public class TransactionManager {

    // Singleton instance
    private static final TransactionManager INSTANCE = new TransactionManager();

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    private final Transaction[] transactions = new Transaction[200];
    private int transactionCount = 0;

    public void addTransaction(Transaction transaction) {
        if (transactionCount < transactions.length) {
            transactions[transactionCount] = transaction;
            transactionCount++;
        } else {
            System.err.println("Transaction Manager storage limit reached. Cannot record new transaction.");
        }
    }

    public void viewTransactionsByAccount(String accountNumber) {
        sortTransaction(); 

        System.out.println("\nTRANSACTION HISTORY FOR ACCOUNT: " + accountNumber);
        System.out.println("-".repeat(85));
        System.out.printf("| %-6s | %-20s | %-13s | %-12s | %-10s |\n",
                "ID", "TIMESTAMP", "TYPE", "AMOUNT", "BALANCE AFTER");
        System.out.println("-".repeat(85));

        boolean foundTransactions = false;

        // Track account-specific totals
        double accountDeposits = 0.0;
        double accountWithdrawals = 0.0;
        
        int matchedCount = 0;
        for (int i = 0; i < transactionCount; i++) {
            Transaction t = transactions[i];

            if (t.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                System.out.printf("| %-6s | %-20s | %-13s | $%-11.2f | $%-9.2f |\n",
                        t.getTransactionId(),
                        t.getFormattedTimestamp(),
                        t.getType().toUpperCase(),
                        t.getAmount(),
                        t.getBalanceAfter()
                );

                matchedCount++;
                foundTransactions = true;
                
                // Accumulate totals based on transaction type
                if (t.getType().equalsIgnoreCase("DEPOSIT") || t.getType().equalsIgnoreCase("Transfer In")) {
                    accountDeposits += t.getAmount();
                } else if (t.getType().equalsIgnoreCase("WITHDRAWAL") || t.getType().equalsIgnoreCase("Transfer Out")) {
                    accountWithdrawals += t.getAmount();
                }
            }
        }
        System.out.println("-".repeat(85));

        // Handle No Transactions Found
        if (!foundTransactions) {
            System.out.println("| No transactions found for this account.                                      |");
            System.out.println("-".repeat(85));
        } else {
            // Display account-specific summary
            System.out.printf("\nTotal Transactions: %d\n", matchedCount);
            System.out.printf("Total Deposits/Transfers In: $%,.2f\n", accountDeposits);
            System.out.printf("Total Withdrawals/Transfers Out: $%,.2f\n", accountWithdrawals);
            double net = accountDeposits - accountWithdrawals;
            System.out.printf("Net Change: $%,.2f\n", net);
            System.out.printf("Total Bank Transactions: %d\n", transactionCount);
        }
    }

    public int getTransactionCount(){
        return transactionCount;
    }

    public void sortTransaction() {
        Arrays.sort(transactions, 0, transactionCount,
                Comparator.comparing(Transaction::getTimestamp).reversed()
        );
    }
}