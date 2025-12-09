package src.services;

import src.models.Account;
import src.models.Transaction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TransactionManager {

    // Singleton instance
    private static final TransactionManager INSTANCE = new TransactionManager();
    private final int MAX_TRANSACTION = 200;

//    static method for getting the instance of the singleton class.
    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        sortTransactions();
        return Arrays.stream(transactions, 0, transactionCount)
                .filter(t -> t.getAccountNumber().equalsIgnoreCase(accountNumber))
                .toList();
    }

//    Ensures only one instance can be created.
    private TransactionManager() {
    }


    final Transaction[] transactions = new Transaction[MAX_TRANSACTION];
    private int transactionCount = 0;

    public void addTransaction(Transaction transaction) {
        if (transactionCount < transactions.length) {
            transactions[transactionCount] = transaction;
            transactionCount++;
        } else {
            System.err.println("Transaction Manager storage limit reached. Cannot record new transaction.");
        }
    }


    public int getTransactionCount(){
        return transactionCount;
    }

    public void sortTransactions() {
        Arrays.sort(transactions, 0, transactionCount,
                Comparator.comparing(Transaction::getTimestamp).reversed()
        );
    }
}