import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Table> table = new ArrayList<>();

    private static void next(Table config, RegularGrammar RG) {
        if (config.getState().equals("q")) {

            //Success
            if (config.getInputStack().isEmpty() && config.getI() == RG.getW().length() + 1) {
                List<String> working = new ArrayList<>(config.getWorkingStack());
                table.add(new Table("f", config.getI(), working, config.getInputStack(), "success"));
                return;
            }
            String head = Character.toString(config.getInputStack().charAt(0));

            //Expand
            if (RG.getNonTerminals().contains(head)) {
                String input = config.getInputStack();
                input = input.substring(1);
                String input2 = RG.findProduction(head).getRHS().get(0) + input;

                head = head + "1";
                List<String> working = new ArrayList<>(config.getWorkingStack());
                working.add(head);
                table.add(new Table("q", config.getI(), working, input2, "Expand"));
            }

            //Advance
            else if (RG.getTerminals().contains(head) && config.getI() <= RG.getW().length() && head.equals(Character.toString(RG.getW().charAt(config.getI() - 1)))) {
                List<String> working = new ArrayList<>(config.getWorkingStack());
                working.add(head);

                String input = config.getInputStack();
                String input2 = input.substring(1);
                table.add(new Table("q", config.getI() + 1, working, input2, "Advance"));
            }

            //Momentary insuccess
            else if (config.getI() > RG.getW().length() || !head.equals(Character.toString(RG.getW().charAt(config.getI() - 1)))) {
                List<String> working = new ArrayList<>(config.getWorkingStack());
                table.add(new Table("b", config.getI(), working, config.getInputStack(), "Momentary insuccess"));
            }
        } else {
            String head = config.getWorkingStack().get(config.getWorkingStack().size() - 1);

            //Back
            if (RG.getTerminals().contains(head)) {
                List<String> working = new ArrayList<>(config.getWorkingStack());
                working.remove(config.getWorkingStack().size() - 1);

                String inputStack = head + config.getInputStack();
                table.add(new Table("b", config.getI() - 1, working, inputStack, "back"));
            }

            //Another try
            else {
                int number = Integer.parseInt(head.substring(1));
                if (RG.findProduction(head.substring(0, 1)).getRHS().size() > number) {
                    String newHead = head.substring(0, 1) + (number + 1);
                    List<String> working = new ArrayList<>(config.getWorkingStack());
                    working.remove(working.size() - 1);
                    working.add(newHead);

                    int currentProdLen = RG.findProduction(head.substring(0, 1)).getRHS().get(number - 1).length();
                    String nextProd = RG.findProduction(head.substring(0, 1)).getRHS().get(number);
                    String inputStack = config.getInputStack();
                    inputStack = inputStack.substring(currentProdLen);
                    String newInputStack = nextProd + inputStack;
                    table.add(new Table("q", config.getI(), working, newInputStack, "another try with q"));
                } else if (config.getI() == 1 && RG.getStartingSymbol().equals(head.substring(0, 1))) {
                    List<String> working = new ArrayList<>(config.getWorkingStack());
                    table.add(new Table("e", config.getI(), working, config.getInputStack(), "error"));
                } else {
                    List<String> working = new ArrayList<>(config.getWorkingStack());
                    working.remove(working.size() - 1);

                    int currentProdLen = RG.findProduction(head.substring(0, 1)).getRHS().get(number - 1).length();
                    String inputStack = config.getInputStack();
                    inputStack = inputStack.substring(currentProdLen);
                    String newInputStack = head.substring(0, 1) + inputStack;

                    table.add(new Table("b", config.getI(), working, newInputStack, "another try with b"));
                }
            }
        }
    }

    private static void recursiveDescendant(RegularGrammar RG) {
        Table initialConfig = new Table("q", 1, new ArrayList<>(), RG.getStartingSymbol());
        table.add(initialConfig);

        while (!table.get(table.size() - 1).getState().equals("e") && !table.get(table.size() - 1).getState().equals("f")) {
            next(table.get(table.size() - 1), RG);
        }

        if (table.get(table.size() - 1).getState().equals("e")) {
            System.out.println("ERROR\n");
            for (Table config : table) {
                System.out.println(config.toString());
            }
        } else {
            System.out.println("Sequence accepted!\n");
            for (Table config : table) {
                System.out.println(config.toString());
            }
        }
    }

    private static void printRGMenu() {
        String s = "\n\t\tPrint:";
        s += "\n\t\t1. Set of non-terminals\n";
        s += "\t\t2. Set of terminals\n";
        s += "\t\t3. Set of productions\n";
        s += "\t\t4. Productions of a given non-terminal\n";
        s += "\t\t5. Input sequence\n";
        s += "\t\t0. Back";
        System.out.println(s);
    }

    private static void printMainMenu() {
        String s = "";
        s += "\n\t1. Print data about the grammar\n";
        s += "\t2. Recursive Descendant\n";
        s += "\t0. Exit";
        System.out.println(s);
    }

    public static void main(String[] args) {
        RegularGrammar RG = new RegularGrammar();

        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            printMainMenu();
            System.out.printf("\t\tOption: ");
            command = scanner.nextLine();
            if (command.equals("1")) {
                String localCommand;
                do {
                    printRGMenu();
                    System.out.printf("\t\t\tOption: ");
                    localCommand = scanner.nextLine();
                    switch (localCommand) {
                        case "1":
                            System.out.println(RG.getNonTerminalsToString());
                            break;
                        case "2":
                            System.out.println(RG.getTerminalsToString());
                            break;
                        case "3":
                            System.out.println(RG.getProductionsToString());
                            break;
                        case "4": {
                            System.out.printf("\t\t\tSymbol: ");
                            String lhs = scanner.nextLine();
                            System.out.println(RG.getProductionToString(lhs));
                            break;
                        }
                        case "5":
                            System.out.println(RG.getWtoString());
                            break;
                        case "0":
                            break;
                        default:
                            System.out.println("Invalid command!\n");
                    }
                } while (!localCommand.equals("0"));
            } else if (command.equals("2")) {
                recursiveDescendant(RG);
            } else if (command.equals("0")) {

            } else {
                System.out.println("Invalid command!\n");
            }
        } while (!command.equals("0"));
    }
}
