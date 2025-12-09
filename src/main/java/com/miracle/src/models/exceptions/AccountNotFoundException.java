package src.models.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accountNumber) {

        super("Account not found: " + accountNumber);
    }

    public AccountNotFoundException(String message, String accountNumber) {
        super(message + ": " + accountNumber);
    }

}
