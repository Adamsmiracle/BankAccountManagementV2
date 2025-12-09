package com.miracle.src.utils;

import java.util.Scanner;

public final class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);


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

    public  static double readDouble(String prompt){
        while(true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try{
                return Float.parseFloat(s);
            }catch (NumberFormatException e){
                System.out.println("Invalid Number, please try again");
            }
        }

    }


    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.println(prompt);
            String answer = scanner.nextLine();
            try{
                if ( answer.equalsIgnoreCase("y")) {
                    return true;
                } else if (answer.equalsIgnoreCase("n")) {
                    return false;
                }
            }catch (Exception e) {
                System.out.println("Enter a valid option");
            }
        }
    }


    public static double validateAmount(){

        return 0.00;
    }



}
