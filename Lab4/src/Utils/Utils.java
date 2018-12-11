package Utils;

import java.util.Scanner;

public class Utils {

    public static void printMenu() {
        System.out.print("\n\t\t1. Read grammar from file.");
        System.out.print("\n\t\t2. Read grammar from keyboard.");
        System.out.print("\n\t\t3. Print grammar.");
        System.out.print("\n\t\t4. Print initial configuration.");
        System.out.print("\n\t\t5. Do computations.");
        System.out.println("\n\t\t0. Exit.");
    }

    public static Integer readOption() {
        System.out.print("\n\t\t\tOption: ");
        Scanner scanner = new Scanner(System.in);
        Integer opt = Integer.valueOf(scanner.nextLine());
        return opt;
    }

    public static String readInput() {
        System.out.print("\n\t\t\tInput: ");
        Scanner scanner = new Scanner(System.in);
        String opt = scanner.nextLine();
        return opt;
    }
}
