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

        if (initialDeposit <= 0){
            throw new InvalidAmountException(initialDeposit);
        }

        // Set initial balance and log as transaction
        if (initialDeposit >= minimumBalance) {
            super.setBalance(initialDeposit);


            
            // Log initial deposit as a transaction
            Transaction initialTransaction = new Transaction(
                this.getAccountNumber(),
                "Deposit",
                initialDeposit,
                initialDeposit
            );
            manager.addTransaction(initialTransaction);
        } else {
            System.err.println("Initial deposit must be positive and greater or equal to $500.00");
        }
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
    public Transaction deposit(double amount) {
        return depositWithType(amount, "Deposit");
    }

    @Override
    public Transaction depositWithType(double amount, String transactionType) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return null;
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
    public Transaction withdrawWithType(double amount, String transactionType) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return null;
        }
        double resultingBalance = this.getBalance() - amount;

        if (resultingBalance < minimumBalance) {
            throw new InsufficientFundsException(
                    " Withdrawal failed. Resulting balance " + resultingBalance +
                            " would violate the minimum balance requirement + "+ minimumBalance + "= .\n"
            );
        }

        super.setBalance(resultingBalance);

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