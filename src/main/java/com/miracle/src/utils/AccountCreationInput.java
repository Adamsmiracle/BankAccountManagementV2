package com.miracle.src.utils;

import com.miracle.src.dto.AccountRequest;
import com.miracle.src.models.SavingsAccount;

public class AccountCreationInput {

    private static final String NAME_REGEX = "^[A-Za-z]{2,}[A-Za-z'-]*(?:\\s[A-Za-z]{2,}[A-Za-z'-]*)+$";
    private static final String CONTACT_REGEX =  "^(?:0(?:24|54|55|59|25|20|50|26|56|57|23)[\\s-]?\\d{3}[\\s-]?\\d{4}"
            + "|(?:\\+233|233|00233)(?:24|54|55|59|25|20|50|26|56|57|23)[\\s-]?\\d{3}[\\s-]?\\d{4})$";
    private static final String ADDRESS_REGEX = "^[A-Za-z0-9][A-Za-z0-9\\s,.'\\-/#]{4,99}$";


    public  static AccountRequest collectAccountCreationData() {

        String usernameErrorMessage = "Enter at least FirstName and SurName";
        String name = ValidationUtils.getValidatedInput("Enter customer name: ", NAME_REGEX, usernameErrorMessage );
        int age = ValidationUtils.getValidAgeInput("Enter customer age: ", "out of a plausible range (18 - 120)");
        String contact = ValidationUtils.getValidatedInput("Enter customer contact: ",CONTACT_REGEX, "Enter valid contact ");
        String address = ValidationUtils.getValidatedInput("Enter customer address: ", ADDRESS_REGEX, "Enter a valid address format (eg: 123 Oak Street, Springfield): ");
        System.out.println("\n");


//        getting the customer type
        System.out.println("Customer type: ");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000) \n");
        int customerType = InputUtils.readInt("Select customer type (1-2): ");
        while (customerType != 1 &&  customerType != 2) {
            System.out.println("Invalid customer type");
            System.out.println("\n");
            customerType = InputUtils.readInt("Select customer type (1-2): ");
            System.out.println("\n");
        }
        System.out.println("\n");


//      Get accountType
        System.out.println("Account type: ");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        int accountType = InputUtils.readInt("Select Type (1-2): ");
        System.out.println("\n");
        while (accountType != 1 &&  accountType != 2) {
            System.out.println("Invalid account type");
            System.out.println("\n");
            accountType = InputUtils.readInt("Select type (1-2): ");
            System.out.println("\n");
        }


//        Read and validate initial deposit
        double initialDeposit;
        while (true){
            initialDeposit = ValidationUtils.getValidAmount("Enter initial Deposit: ");
            if (accountType == 1 && initialDeposit < SavingsAccount.getMinimumBalance()) {
                System.out.println("Savings account requires minimum initial deposit of $500.00");
                continue;
            }
            System.out.println("\n");
            break;
        }


        return new AccountRequest(name, age, contact, address, customerType, accountType, initialDeposit);

    }
}
