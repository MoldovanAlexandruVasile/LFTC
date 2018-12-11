package Utils;

import java.util.*;

import static Utils.GrammarUtils.NTerminals;
import static Utils.GrammarUtils.productions;

public class RecDescendantUtils {
    public static String input;
    public static String currentState = "q";
    public static Integer i = 1;
    public static Stack<String> alpha = new Stack<>();
    public static Stack<String> beta = new Stack<>();
    private static List<String> usedNonTerminals = new ArrayList<>();
    private static String a;
    private static Integer j = 0;

    public static void run() {
        Integer size = input.length();
        Integer index = 0;
        while (!currentState.equals("f") && !currentState.equals("e")) {
            if (currentState.equals("q")) {
                if (beta.empty() && i == size + 1) {
                    currentState = "f";
                    System.out.println("q - 1");
                } else {
                    String betaTop = beta.peek();
                    if (NTerminals.contains(betaTop)) {
                        if (usedNonTerminals.contains(betaTop))
                            index++;
                        else {
                            usedNonTerminals.add(betaTop);
                            index = 0;
                        }
                        String newNT = betaTop + String.valueOf(index);
                        index++;
                        alpha.push(newNT);
                        beta.pop();
                        beta.push(productions.get(betaTop).get(0));
                        System.out.println("q - 2");
                    } else if (beta.peek().equals(String.valueOf(input.charAt(i - 1)))) {
                        i++;
                        alpha.push(betaTop);
                        a = beta.pop();
                        System.out.println("q - 3");
                    } else {
                        currentState = "r";
                        System.out.println("q - 4");
                    }
                }
            } else if (currentState.equals("r")) {
                String alphaTop = alpha.peek();
                if (alphaTop.equals(a)) {
                    i--;
                    a = alpha.pop();
                    beta.push(a);
                    System.out.println("r - 1");
                } else {
                    List<String> prod = productions.get(a);
                    String betaTop = beta.peek();
                    String terminal = productions.get(a).get(j);
                    if (prod.size() > j && a.equals(alphaTop) && betaTop.equals(terminal)) {
                        currentState = "q";
                        j++;
                        alpha.pop();
                        beta.pop();
                        String newNT = betaTop + String.valueOf(j);
                        String newT = productions.get(a).get(j);
                        alpha.push(newNT);
                        beta.push(newT);
                        System.out.println("r - 2");
                    } else {
                        String initialConfig = getInitialConfig(productions);
                        if (i == 1 && a.equals(initialConfig))
                            currentState = "e";
                        else {
                            alpha.pop();
                            beta.push(a);
                        }
                        System.out.println("r - 3");
                    }
                }
            } else if (currentState.equals("e")) {
                System.out.println("\n\t\tERROR !");
            } else {
                System.out.println("\n\t\tThe sequence is accepted !");
            }
        }
    }

    public static void initRecDescendant(HashMap<String, List<String>> productions) {
        populateBeta(productions);
    }

    public static void printAllData() {
        System.out.println("\t\t\tCurrent state: " + currentState);
        System.out.println("\t\t\ti = " + i);
        printAlpha();
        printBeta();
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

    private static String getInitialConfig(HashMap<String, List<String>> productions) {
        Iterator it = productions.entrySet().iterator();
        String config = "";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            config += (String) pair.getKey();
            List<String> values = (List) pair.getValue();
            for (Integer pos = 0; pos < values.size(); pos++)
                config += values.get(pos);
        }
        return config;
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
