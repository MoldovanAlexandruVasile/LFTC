package Utils;

import java.util.*;

import static Utils.GrammarUtils.*;

public class RecDescendantUtils {

    public static List<String> states = new ArrayList<>();
    public static String currentState;
    public static Integer i = 1;
    public static Stack<String> alpha = new Stack<>();
    public static Stack<String> beta = new Stack<>();

    public static void run() {
        while (beta.empty() && (i == beta.size() + 1)) {
            if (currentState.equals("q")) {
                if (beta.empty() && i == beta.size() + 1)
                    currentState = "f";
                else {
                    String betaTop = beta.peek();
                    if (NTerminals.contains(betaTop)) {
                        alpha.push(betaTop);
                        beta.pop();
                        beta.push(productions.get(betaTop).get(0));
                    } else if (ETerminals.contains(betaTop)) {
                        i++;
                        alpha.push(betaTop);
                        beta.pop();
                    } else currentState = "r";
                }
            } else if (currentState.equals("r")) {

            } else if (currentState.equals("e")) {
                System.out.println("\n\t\tERROR !");
            } else {
                System.out.println("\n\t\tThe sequence is accepted !");
            }
        }
    }

    public static void initRecDescendant(HashMap<String, List<String>> productions) {
        initStates();
        populateAlpha(productions);
        populateBeta(productions);
    }

    public static void printAllData() {
        System.out.println("\t\t\tCurrent state: " + currentState);
        System.out.println("\t\t\ti = " + i);
        printAlpha();
        printBeta();
    }

    private static void populateAlpha(HashMap<String, List<String>> productions) {

    }

    private static void initStates() {
        currentState = "q";
        states.add("q");
        states.add("b");
        states.add("f");
        states.add("e");
    }

    private static void populateBeta(HashMap<String, List<String>> productions) {
        Iterator it = productions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            beta.push((String) pair.getKey());
            List<String> values = (List) pair.getValue();
            for (Integer pos = 0; pos < values.size(); pos++)
                beta.push(values.get(pos));
        }
    }

    private static void printAlpha() {
        System.out.print("\t\t\tAlpha: ");
        String values = "";
        for (String element : alpha) {
            values += element + " ";
        }
        System.out.println(values);
    }

    private static void printBeta() {
        System.out.print("\t\t\tBeta: ");
        String values = "";
        for (String element : beta) {
            values += element + " ";
        }
        System.out.println(values);
    }
}
