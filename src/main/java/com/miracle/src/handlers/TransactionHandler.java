package com.miracle.src.handlers;
import com.miracle.src.models.exceptions.AccountNotFoundException;
import com.miracle.src.models.exceptions.InsufficientFundsException;
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

    public void handleTransaction() {
        try {
            TransactionProcessingInput transactionProcessingInput = new TransactionProcessingInput();
            TransactionRequest request = TransactionProcessingInput.processTransactionMain();

            if (request == null) {
                System.out.println("Transaction cancelled by user.");
                return;
            }

            manager.processTransaction(request);
            System.out.println("✔ Transaction processed successfully!");

        } catch (InvalidAmountException e) {
            System.err.println("Transaction failed: " + e.getMessage());
        } catch (OverdraftExceededException e) {
            System.err.println("Transaction failed: " + e.getMessage());
        } catch (AccountNotFoundException e) {
            System.err.println("Transaction failed: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.err.println("Transaction failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
