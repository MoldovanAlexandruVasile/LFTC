import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {

    private static List<String> G, NTerminals, ETerminals;
    private static HashMap<String, List<String>> productions = new HashMap<>();
    private static String startingSymbol;
    private static List<String> QStates = new ArrayList<>();
    private static List<String> ESymbols = new ArrayList<>();
    private static List<String> F = new ArrayList<>();
    private static String initialState;
    private static HashMap<List<String>, String> transitions = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("\n\t\t\t~MENU~");
        while (true) {
            printMenu();
            Integer option = readOption();
            if (option == 1) {
                initGrammar();
                productions = readGrammarFromFile();
            }
            if (option == 2) {
                initGrammar();
                productions = readGrammarFromKeyboard();
            }
            if (option == 3) {
                printList("G");
                printList("NTerminals");
                printList("ETerminals");
                System.out.println("\t\t\tStarting symbol: " + startingSymbol);
                printTerminals(productions);
            }
            if (option == 4) {
                Boolean isReg;
                isReg = verifyGrammar(productions);
                if (isReg)
                    System.out.println("\t\t\tIt is a regular grammar.");
                else
                    System.out.println("\t\t\tIt is not regular grammar.");
            }
            if (option == 5) {
                transitions = readAutomatonFromFile();
            }
            if (option == 6) {
                printList("QStates");
                printList("ESymbols");
                printList("F");
                printList("initialState");
                printTransitions();
            }
            if (option == 7) {
                grammarToAutomaton(productions);
                printList("QStates");
                printList("ESymbols");
                printList("F");
                System.out.println("\t\t\tInitial state: " + initialState);
                printTransitions();
            }
            if (option == 8) {
                initGrammar();
                HashMap<String[], List<String>> newMap = automatonToGrammar(transitions);
                printList("NTerminals");
                printList("ETerminals");
                System.out.println("\t\t\tStarting symbol: " + startingSymbol);
                printTerminalsV2(newMap);
            }
            if (option == 0) {
                break;
            }
        }
    }

    private static void printTransitions() {
        for (List<String> t : transitions.keySet()) {
            List<String> key = t;
            String value = transitions.get(key);
            System.out.println("\t\t\tT(" + key.get(0) + ", " + key.get(1) + ") = " + value);
        }
    }

    private static HashMap<List<String>, String> readAutomatonFromFile() {
        HashMap<List<String>, String> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File("Automaton.txt")));
            for (int i = 1; i <= 3; i++) {
                line = br.readLine();
                if (line.contains("Q"))
                    addToAutomatonList("Q", line);
                else if (line.contains("E"))
                    addToAutomatonList("E", line);
                else if (line.contains("F"))
                    addToAutomatonList("F", line);
            }
            line = br.readLine();
            line = line.replaceAll("( )+", "");
            String[] lineSplit = line.split("=");
            initialState = lineSplit[1];
            line = br.readLine();
            while (line != null) {
                line = line.replaceAll("( )+", "");
                lineSplit = line.split("=");
                List<String> t = new ArrayList<>();
                t.add(String.valueOf(lineSplit[0].charAt(2)));
                t.add(String.valueOf(lineSplit[0].charAt(4)));
                result.put(t, lineSplit[1]);
                line = br.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static void printTerminals(HashMap<String, List<String>> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<String> values = (List) pair.getValue();
            String valuesS = "";
            Integer i;
            for (i = 0; i < values.size() - 1; i++) {
                valuesS += values.get(i) + " | ";
            }
            valuesS += values.get(i);
            System.out.println("\t\t\t" + pair.getKey() + " -> " + valuesS);
        }
    }

    private static void printTerminalsV2(HashMap<String[], List<String>> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<String> values = (List) pair.getValue();
            String valuesS = "";
            Integer i;
            for (i = 0; i < values.size() - 1; i++) {
                valuesS += values.get(i) + " | ";
            }
            valuesS += values.get(i);
            String[] elems = (String[]) pair.getKey();
            String key = elems[1];
            System.out.println("\t\t\t" + key + " -> " + valuesS);
        }
    }

    private static void initGrammar() {
        G = new ArrayList<>();
        NTerminals = new ArrayList<>();
        ETerminals = new ArrayList<>();
        productions = new HashMap<>();
        startingSymbol = "";
    }

    private static HashMap<String[], List<String>> automatonToGrammar(HashMap<List<String>, String> transitions) {
        HashMap<String[], List<String>> thisMap = new HashMap<>();
        Integer k = 0;
        startingSymbol = initialState;
        for (Integer pos = 0; pos < QStates.size(); pos++)
            NTerminals.add(QStates.get(pos));
        for (Integer pos = 0; pos < ESymbols.size(); pos++)
            ETerminals.add(ESymbols.get(pos));
        Iterator it = transitions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry transitionKey = (Map.Entry) it.next();
            List<String> keyMap = (List) transitionKey.getKey();
            List<String> valueForMap = new ArrayList<>();
            valueForMap.add(keyMap.get(1));
            valueForMap.add(transitionKey.getValue().toString());
            String[] elems = new String[2];
            elems[0] = String.valueOf(k);
            elems[1] = keyMap.get(0);
            thisMap.put(elems, valueForMap);
        }
        return thisMap;
    }

    private static void grammarToAutomaton(HashMap<String, List<String>> map) {
        initialState = startingSymbol;
        for (Integer pos = 0; pos < NTerminals.size(); pos++)
            QStates.add(NTerminals.get(pos));
        for (Integer pos = 0; pos < ETerminals.size(); pos++)
            ESymbols.add(ETerminals.get(pos));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry grammarPair = (Map.Entry) it.next();
            List<String> grammarPairList = (List) grammarPair.getValue();
            if (grammarPairList.contains("E") && !F.contains(grammarPair.getKey().toString())) {
                F.add(grammarPair.getKey().toString());
                for (Integer i = 0; i < grammarPairList.size() - 1; i++) {
                    if (grammarPairList.get(i).length() == 2) {
                        List<String> key = new ArrayList<>();
                        key.add(grammarPair.getKey().toString());
                        key.add(Character.toString(grammarPairList.get(i).charAt(0)));
                        transitions.put(key, Character.toString(grammarPairList.get(i).charAt(1)));
                    }
                    if (grammarPairList.get(i).length() == 1) {
                        List<String> key = new ArrayList<>();
                        key.add(grammarPair.getKey().toString());
                        key.add(Character.toString(grammarPairList.get(i).charAt(0)));
                        transitions.put(key, "K");
                    }
                }
            } else {
                for (Integer i = 0; i < grammarPairList.size(); i++) {
                    if (grammarPairList.get(i).length() == 2) {
                        List<String> key = new ArrayList<>();
                        key.add(grammarPair.getKey().toString());
                        key.add(grammarPairList.get(0));
                        transitions.put(key, grammarPairList.get(1));
                    }
                    if (grammarPairList.get(i).length() == 1) {
                        List<String> key = new ArrayList<>();
                        key.add(grammarPair.getKey().toString());
                        key.add(grammarPairList.get(0));
                        transitions.put(key, "K");
                    }
                }
            }
        }
    }

    private static HashMap<String, List<String>> readGrammarFromKeyboard() {
        System.out.print("\n\t\tG = {N, E, productions, S}");
        G.add("N");
        G.add("E");
        G.add("productions");
        G.add("S");
        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tN = ");
        Scanner scanner = new Scanner(System.in);
        String myN = scanner.nextLine();
        myN = myN.replaceAll("( )+", "");
        String[] listN = myN.split(",");
        for (Integer number = 0; number < listN.length; number++)
            NTerminals.add(listN[number]);
        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tE = ");
        scanner = new Scanner(System.in);
        String myE = scanner.nextLine();
        myE = myE.replaceAll("( )+", "");
        String[] listE = myE.split(",");
        for (Integer number = 0; number < listE.length; number++)
            ETerminals.add(listE[number]);
        System.out.print("\n\t\tStarting symbol = ");
        scanner = new Scanner(System.in);
        String myP = scanner.nextLine();
        startingSymbol = myP;
        System.out.print("\n\t\tInsert '0' when you finished to add to that specific component\n");
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> list;
        for (Integer pos = 0; pos < NTerminals.size(); pos++) {
            list = new ArrayList<>();
            while (true) {
                System.out.print("\t\t\t" + NTerminals.get(pos) + " -> ");
                Scanner reader = new Scanner(System.in);
                String stuff = reader.nextLine();
                if (stuff.equals("0"))
                    break;
                list.add(stuff);
            }
            map.put(NTerminals.get(pos), list);
        }
        return map;
    }

    private static void printList(String character) {
        if (character.equals("G")) {
            String string = "\t\t\tG = {";
            Integer i;
            for (i = 0; i < G.size() - 1; i++)
                string += G.get(i) + ", ";
            string += G.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("NTerminals")) {
            String string = "\t\t\tN = {";
            Integer i;
            for (i = 0; i < NTerminals.size() - 1; i++)
                string += NTerminals.get(i) + ", ";
            string += NTerminals.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("ETerminals")) {
            String string = "\t\t\tE = {";
            Integer i;
            for (i = 0; i < ETerminals.size() - 1; i++)
                string += ETerminals.get(i) + ", ";
            string += ETerminals.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("QStates")) {
            String string = "\t\t\tQ = {";
            Integer i;
            for (i = 0; i < QStates.size() - 1; i++)
                string += QStates.get(i) + ", ";
            string += QStates.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("ESymbols")) {
            String string = "\t\t\tE = {";
            Integer i;
            for (i = 0; i < ESymbols.size() - 1; i++)
                string += ESymbols.get(i) + ", ";
            string += ESymbols.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("F")) {
            String string = "\t\t\tF = {";
            Integer i;
            for (i = 0; i < F.size() - 1; i++)
                string += F.get(i) + ", ";
            string += F.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("initialState")) {
            System.out.println("\t\t\tQ0 = " + initialState);
        }
    }

    private static Boolean verifyGrammar(HashMap<String, List<String>> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<String> rightSide = (List) pair.getValue();
            if (rightSide.size() > 2)
                return false;
            if (rightSide.size() == 2) {
                if (rightSide.get(0).length() != 1)
                    return false;
                if (rightSide.get(1).contains("E"))
                    return false;
                if (rightSide.get(1).length() == 2) {
                    if (rightSide.get(1).charAt(0) < 97 || rightSide.get(1).charAt(0) > 122)
                        return false;
                    if (!ETerminals.contains(String.valueOf(rightSide.get(1).charAt(0))))
                        return false;
                    if (rightSide.get(1).charAt(1) < 65 || rightSide.get(1).charAt(1) > 90)
                        return false;
                    if (!NTerminals.contains(String.valueOf(rightSide.get(1).charAt(1))))
                        return false;
                } else if (rightSide.get(1).length() == 1) {
                    if (!ETerminals.contains(rightSide.get(1)))
                        return false;
                    if (NTerminals.contains(rightSide.get(1)))
                        return false;
                }
            } else if (rightSide.size() == 1) {
                if (rightSide.get(0).equals("E") && !pair.getKey().equals("E"))
                    return false;
                if (!ETerminals.contains(rightSide.get(0)) && !pair.getKey().equals("S"))
                    return false;
            }
        }
        return true;
    }

    private static HashMap<String, List<String>> readGrammarFromFile() {
        HashMap<String, List<String>> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File("Grammar.txt")));
            for (int i = 1; i <= 3; i++) {
                line = br.readLine();
                if (line.contains("G"))
                    addToGrammarList("G", line);
                else if (line.contains("N"))
                    addToGrammarList("N", line);
                else if (line.contains("E"))
                    addToGrammarList("E", line);
            }
            line = br.readLine();
            startingSymbol = line;
            for (Integer pos = 0; pos < NTerminals.size(); pos++)
                result.put(NTerminals.get(pos), new ArrayList<>());
            line = br.readLine();
            line = line.replaceAll("( )+", "");
            while (line != null) {
                line = line.replaceAll("( )+", "");
                String[] lineSplit = line.split("->");
                Iterator it = result.entrySet().iterator();
                Map.Entry pair = null;
                while (it.hasNext()) {
                    pair = (Map.Entry) it.next();
                    if (lineSplit[0].equals(pair.getKey()))
                        break;
                }
                assert pair != null;
                String key = (String) pair.getKey();
                List<String> values = (List) pair.getValue();
                values.add(lineSplit[1]);
                result.put(key, values);
                line = br.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static Integer readOption() {
        System.out.print("\n\t\t\tOption: ");
        Scanner scanner = new Scanner(System.in);
        Integer opt = Integer.valueOf(scanner.nextLine());
        return opt;
    }

    private static void printMenu() {
        System.out.print("\n\t\t1. Read grammar from file.");
        System.out.print("\n\t\t2. Read grammar from keyboard.");
        System.out.print("\n\t\t3. Print grammar.");
        System.out.print("\n\t\t4. Verify if grammar is regular.");
        System.out.print("\n\t\t5. Read automaton from file.");
        System.out.print("\n\t\t6. Display automaton.");
        System.out.print("\n\t\t7. Grammar to Automaton.");
        System.out.print("\n\t\t8. Automaton to Grammar.");
        System.out.println("\n\t\t0. Exit.");
    }

    private static void addToGrammarList(String character, String line) {
        int start = line.indexOf("{") + 1;
        int end = line.indexOf("}");
        String[] elements = line.substring(start, end).replaceAll("( )+", "").split(",");
        for (int pos = 0; pos < elements.length; pos++) {
            if (character.equals("G"))
                G.add(elements[pos]);
            else if (character.equals("N"))
                NTerminals.add(elements[pos]);
            else if (character.equals("E"))
                ETerminals.add(elements[pos]);
        }
    }

    private static void addToAutomatonList(String string, String line) {
        int start = line.indexOf("{") + 1;
        int end = line.indexOf("}");
        String[] elements = line.substring(start, end).replaceAll("( )+", "").split(",");
        for (int pos = 0; pos < elements.length; pos++) {
            if (string.equals("Q"))
                QStates.add(elements[pos]);
            else if (string.equals("E"))
                ESymbols.add(elements[pos]);
            else if (string.equals("F"))
                F.add(elements[pos]);
        }
    }
}


