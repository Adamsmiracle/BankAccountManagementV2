package com.miracle.src.models;

import com.miracle.src.models.exceptions.InsufficientFundsException;
import com.miracle.src.models.exceptions.InvalidAmountException;
import com.miracle.src.services.TransactionManager;

public class SavingsAccount extends Account {

    // --- Private Fields
    private final double interestRate;
    private static double minimumBalance = 500.00;
    private final TransactionManager manager = TransactionManager.getInstance();

    public SavingsAccount(Customer customer, double initialDeposit) throws InvalidAmountException {
        super(customer);
        this.interestRate = 0.035;
        this.setStatus("Active");
        this.minimumBalance = 500.00;

        if (initialDeposit <= 0) {
            throw new InvalidAmountException(initialDeposit);
        }

        if (initialDeposit < minimumBalance) {
            throw new InvalidAmountException(
                    "Initial deposit must be at least $" + minimumBalance,
                    initialDeposit
            );
        }

        super.setBalance(initialDeposit);

        Transaction initialTransaction = new Transaction(
                this.getAccountNumber(),
                "Deposit",
                initialDeposit,
                initialDeposit
        );
        manager.addTransaction(initialTransaction);
    }



    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    protected void displaySpecificDetails() {
        System.out.printf("Interest Rate: %.1f%%\n", getInterestRate() * 100);
        System.out.printf("Minimum Balance: $%,.2f\n", getMinimumBalance());
    }

    @Override
    public Transaction deposit(double amount) throws InvalidAmountException {
        return depositWithType(amount, "Deposit");
    }

    @Override
    public Transaction depositWithType(double amount, String transactionType) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        if (transactionType == null || transactionType.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be null or empty");
        }

        this.setBalance(this.getBalance() + amount);

        Transaction newTransaction = new Transaction(
                this.getAccountNumber(),
                transactionType,
                amount,
                this.getBalance()
        );

        manager.addTransaction(newTransaction);
        return newTransaction;
    }

    @Override
    public Transaction withdraw(double amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0){
            throw new InvalidAmountException(amount);
        }
        return withdrawWithType(amount, "Withdrawal");
    }

    @Override
    public Transaction withdrawWithType(double amount, String transactionType)
            throws InvalidAmountException, InsufficientFundsException {

        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        // Validate transaction type
        if (transactionType == null || transactionType.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be null or empty");
        }

        // Calculate resulting balance
        double resultingBalance = this.getBalance() - amount;

        // Check against minimum balance
        if (resultingBalance < minimumBalance) {
            throw new InsufficientFundsException(
                    String.format(
                            "Withdrawal failed. Resulting balance ($%.2f) would violate minimum balance requirement ($%.2f)",
                            resultingBalance, minimumBalance
                    )
            );
        }



        // Update account balance
        super.setBalance(resultingBalance);

        // Record the transaction
        Transaction newTransaction = new Transaction(
                this.getAccountNumber(),
                transactionType,
                amount,
                resultingBalance
        );
        manager.addTransaction(newTransaction);

        return newTransaction;
    }



    public double calculateInterest() {
        return this.getInterestRate() * super.getBalance();
    }

    public double getInterestRate() {
        return interestRate;
    }

    public static double getMinimumBalance() {
        return minimumBalance;
    }
}