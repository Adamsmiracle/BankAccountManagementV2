package com.miracle.src.handlers;

import com.miracle.src.services.AccountManager;
import com.miracle.src.dto.AccountRequest;

import com.miracle.src.models.SavingsAccount;
import com.miracle.src.models.exceptions.InvalidAmountException;
import com.miracle.src.models.exceptions.OverdraftExceededException;
import com.miracle.src.services.AccountManager;
import com.miracle.src.utils.AccountCreationInput;
import com.miracle.src.utils.InputUtils;
import com.miracle.src.utils.ValidationUtils;


public class AccountCreationHandler {
    private final AccountManager accountManager = AccountManager.getInstance();

    public AccountCreationHandler() {

    }

    public void handleCreateAccount() throws InvalidAmountException, OverdraftExceededException {
        AccountRequest request = AccountCreationInput.collectAccountCreationData();
        accountManager.createAccount(request);
    }
}

