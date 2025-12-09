package src.handlers;

import src.dto.AccountRequest;

import src.models.SavingsAccount;
import src.models.exceptions.InvalidAmountException;
import src.models.exceptions.OverdraftExceededException;
import src.services.AccountManager;
import src.utils.AccountCreationInput;
import src.utils.InputUtils;
import src.utils.ValidationUtils;


public class AccountCreationHandler {
    private final AccountManager accountManager;

    public AccountCreationHandler(AccountManager manager) {
        this.accountManager = manager;
    }

    public void handleCreateAccount() throws InvalidAmountException, OverdraftExceededException {
        AccountRequest request = AccountCreationInput.collectAccountCreationData();
        accountManager.createAccount(request);
    }
}

