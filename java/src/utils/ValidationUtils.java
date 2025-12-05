package src.utils;

import java.util.Scanner;
import java.util.regex.Pattern;

import static src.utils.InputUtils.*;

public final class ValidationUtils {



    public static String getValidatedInput(String prompt, String regexString, String errorMessage) {

//        Compile the regular expression for optimization
        final Pattern pattern = Pattern.compile(regexString);
        String input;
        while (true) {
            input = InputUtils.readLine(prompt);

            // Check for empty or null input.
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            // Perform the Regex Validation Check
            if (pattern.matcher(input).matches()) {
                return input; // Success!
            } else {
                System.out.println("Invalid format. " + errorMessage);
            }
        }
    }


    public static int getValidAgeInput(String prompt, String errorMessage){
        int age;
        while (true){
                age = InputUtils.readInt(prompt);
                if (age > 0 && age < 120) {
                    return age;
                }
            System.out.println(errorMessage);
        }
    }

    public static String getValidAccountNumber(String transfer) {
        final String ACCOUNT_REGEX = "^ACC\\d{3}$";
        while (true) {
            String accountNumber;
            if (transfer.equalsIgnoreCase("transfer")) {
                accountNumber = readLine("Enter the recipient's Account Number: ");
            } else {
                accountNumber = readLine("Enter Account Number (e.g., ACC001): ").trim().toUpperCase();
            }
            if (accountNumber.matches(ACCOUNT_REGEX)) return accountNumber;
            System.out.println("Invalid account format. Account number must be ACC followed by three digits.");
        }
    }



}
