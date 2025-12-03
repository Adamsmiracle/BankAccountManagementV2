public abstract class Account implements Transactable {
    public static int accountCounter = 0;

    //    private field
    private String accountNumber;
    private double balance;
    private String status = "Active";
    private final Customer customer;


    public Account(Customer customer) {
        accountCounter++;
        this.customer = customer;
        this.accountNumber = String.format("ACC%03d", accountCounter);
    }

    // Abstract methods for deposit and withdraw
    public abstract Transaction deposit(double amount);
    public abstract Transaction withdraw(double amount);
    
    // Overloaded methods with transaction type for transfers
    public abstract Transaction depositWithType(double amount, String transactionType);
    public abstract Transaction withdrawWithType(double amount, String transactionType);

    // GETTERS
    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    // SETTERS
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ABSTRACT METHODS
    public abstract String getAccountType();
    
    // Template method for displaying account details
    public void displayAccountDetails() {
        System.out.println("✔ Account created successfully!");
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Customer: " + getCustomer().getCustomerId() + " - " + 
                          getCustomer().getName() + " (" + getCustomer().getCustomerType() + ")");
        System.out.println("Account Type: " + getAccountType());
        System.out.printf("Initial Balance: $%,.2f\n", getBalance());
        
        // Call abstract method for account-specific details
        displaySpecificDetails();
        
        System.out.println("Status: " + getStatus());
        System.out.println("\n");
    }
    
    // Abstract method for subclass-specific details
    protected abstract void displaySpecificDetails();

    // IMPLEMENTATION OF TRANSACTABLE INTERFACE
    @Override
    public boolean processTransaction(double amount, String type) {
        if (amount < 0) {
            return false;
        }
        
        Transaction result = null;
        
        if (type.equalsIgnoreCase("Deposit")) {
            result = this.deposit(amount);
        } else if (type.equalsIgnoreCase("Withdrawal")) {
            result = this.withdraw(amount);
        } else if (type.equalsIgnoreCase("Transfer Out")) {
            result = this.withdrawWithType(amount, "Transfer Out");
        } else if (type.equalsIgnoreCase("Transfer In")) {
            result = this.depositWithType(amount, "Transfer In");
        }
        
        return result != null;
    }
}