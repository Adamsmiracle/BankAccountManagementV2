public class CheckingAccount extends Account {

    private double overDraftLimit;
    private double monthlyFee;
    private TransactionManager manager = TransactionManager.getInstance();

    public CheckingAccount(Customer customer, double initialDeposit) {
        super(customer);
        this.overDraftLimit = 1000.00;
        this.monthlyFee = 10.00;
        this.setStatus("Active");
        
        // Set balance and log initial deposit as transaction
        setBalance(initialDeposit);
        
        // Log initial deposit as a transaction
        Transaction initialTransaction = new Transaction(
            this.getAccountNumber(),
            "Deposit",
            initialDeposit,
            initialDeposit
        );
        manager.addTransaction(initialTransaction);
    }

 

    @Override
    protected void displaySpecificDetails() {
        System.out.printf("Overdraft Limit: $%,.2f\n", getOverDraftLimit());
        
        if (getCustomer() instanceof PremiumCustomer && ((PremiumCustomer) getCustomer()).hasWaivedFees()) {
            System.out.println("Monthly Fee: Waived (Premium customer)");
        } else {
            System.out.printf("Monthly Fee: $%,.2f\n", getMonthlyFee());
        }
    }

    @Override
    public Transaction deposit(double amount) {
        if (amount <= 0) {
            System.out.println("The amount must be a positive value");
            return null;
        }

        this.setBalance(this.getBalance() + amount);
        Transaction newTransaction = new Transaction(
                this.getAccountNumber(),
                "Deposit",
                amount,
                this.getBalance()
        );
        manager.addTransaction(newTransaction);
        return newTransaction;
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    
    @Override
    public Transaction withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return null;
        }
        if (super.getBalance() - amount >= -overDraftLimit) {
            this.setBalance(this.getBalance() - amount);

            Transaction newTransaction = new Transaction(
                this.getAccountNumber(),
                "Withdrawal",
                amount,
                this.getBalance()
            );
            manager.addTransaction(newTransaction);
            
            System.out.printf("Withdrawal successful. New balance: $%,.2f\n", this.getBalance());
            return newTransaction;

        } else {
            System.out.printf("Withdrawal failed. Resulting balance ($%,.2f) would exceed the overdraft limit ($%,.2f).\n",
                    (super.getBalance() - amount),
                    overDraftLimit
            );
            return null;
        }
    }

    public boolean applyMonthlyFee() {
        Customer c = getCustomer();
        if (c instanceof PremiumCustomer && ((PremiumCustomer) c).hasWaivedFees()) {
            System.out.println("Monthly fee waived for Premium customer.");
            return true;
        }

        if (super.getBalance() - monthlyFee >= -overDraftLimit) {
            super.setBalance(super.getBalance() - monthlyFee);
            return true;
        }
        return false;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public double getOverDraftLimit() {
        return overDraftLimit;
    }



    @Override
    public Transaction depositWithType(double amount, String transactionType) {
        // TODO Auto-generated method
        throw new UnsupportedOperationException("Unimplemented method 'depositWithType'");
    }



    @Override
    public Transaction withdrawWithType(double amount, String transactionType) {
        // TODO Auto-generated method
        throw new UnsupportedOperationException("Unimplemented method 'withdrawWithType'");
    }

}