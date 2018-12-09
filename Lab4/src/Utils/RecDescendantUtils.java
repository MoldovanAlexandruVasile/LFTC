package Utils;

import java.util.*;

public class RecDescendantUtils {

    public static List<Character> states = new ArrayList<>();
    public static Character currentState;
    public static Integer i = 1;
    public static Stack<String> alpha = new Stack<>();
    public static Stack<String> beta = new Stack<>();

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
        currentState = 'q';
        states.add('q');
        states.add('b');
        states.add('f');
        states.add('e');
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
