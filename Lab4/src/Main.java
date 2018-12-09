import static Utils.GrammarUtils.*;
import static Utils.RecDescendantUtils.*;
import static Utils.Utils.printMenu;
import static Utils.Utils.readOption;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n\t\t\t~MENU~");
        while (true) {
            printMenu();
            Integer option = readOption();
            if (option == 1) {
                initGrammar();
                productions = readGrammarFromFile();
                initRecDescendant(productions);
            } else if (option == 2) {
                initGrammar();
                productions = readGrammarFromKeyboard();
                initRecDescendant(productions);
            } else if (option == 3) {
                System.out.println();
                printList("G");
                printList("NTerminals");
                printList("ETerminals");
                System.out.println("\t\t\tStarting symbol: " + startingSymbol);
                printTerminals(productions);
            } else if (option == 4) {
                System.out.println();
                printAllData();
            } else if (option == 0) {
                break;
            }
        }
    }
}


