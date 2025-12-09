package src.handlers;
import src.dto.TransactionRequest;
import src.models.exceptions.InvalidAmountException;
import src.models.exceptions.OverdraftExceededException;
import src.services.*;
import src.utils.TransactionProcessingInput;

public class TransactionHandler {
    private static AccountManager manager;
    public TransactionHandler(AccountManager manager) {
        this.manager = manager;
    }

    public void handleTransaction () throws InvalidAmountException, OverdraftExceededException {
//        TransactionProcessingInput transactionProcessingInput = new TransactionProcessingInput(manager);
        TransactionRequest request = TransactionProcessingInput.processTransactionMain();
        if (request == null) {
            // User exited, nothing to process
            System.out.println("Transaction cancelled by user. \n");
            return;
        }
        manager.processTransaction(request);
    }
}
