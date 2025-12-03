public class SavingsAccount extends Account {

    // --- Private Fields
    private final double interestRate;
    private final double minimumBalance;
    private final TransactionManager manager = TransactionManager.getInstance();

    public SavingsAccount(Customer customer, double initialDeposit) {
        super(customer);
        this.interestRate = 0.035;
        this.minimumBalance = 500.00;
        this.setStatus("Active");

        // Set initial balance and log as transaction
        if (initialDeposit >= this.minimumBalance) {
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
    public Transaction withdraw(double amount) {
        return withdrawWithType(amount, "Withdrawal");
    }

    @Override
    public Transaction withdrawWithType(double amount, String transactionType) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return null;
        }
        double resultingBalance = this.getBalance() - amount;

        if (resultingBalance < this.minimumBalance) {
            System.out.printf(
                    " Withdrawal failed. Resulting balance ($%.2f)" +
                            " would violate the minimum balance requirement ($%.2f).\n",
                    resultingBalance,
                    this.minimumBalance
            );
            return null;
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

    public double getMinimumBalance() {
        return minimumBalance;
    }
}