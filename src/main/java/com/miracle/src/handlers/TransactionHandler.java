package com.miracle.src.handlers;
import com.miracle.src.services.AccountManager;
import com.miracle.src.dto.TransactionRequest;
import com.miracle.src.models.exceptions.InvalidAmountException;
import com.miracle.src.models.exceptions.OverdraftExceededException;
import com.miracle.src.services.*;
import com.miracle.src.utils.TransactionProcessingInput;

public class TransactionHandler {
    private static AccountManager manager = AccountManager.getInstance();
    public TransactionHandler() {

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
