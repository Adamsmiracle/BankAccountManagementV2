import java.util.Scanner;

public final class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtils() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }


    public static String getValidAccountNumber(String transfer) {
        final String ACCOUNT_REGEX = "^ACC\\d{3}$";
        while (true) {
            String account;
            if (transfer.equalsIgnoreCase("transfer")) {
                account = readLine("Enter the recipient's Account Number: ");
            } else {
                account = readLine("Enter Account Number (e.g., ACC001): ").trim().toUpperCase();
            }
            if (account.matches(ACCOUNT_REGEX)) return account;
            System.out.println("Invalid account format. Account number must be ACC followed by three digits.");
        }
    }
}
