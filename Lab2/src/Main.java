import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {

    private static List<String> G = new ArrayList<>();
    private static List<String> N = new ArrayList<>();
    private static List<String> E = new ArrayList<>();
    private static List<String> P = new ArrayList<>();
    private static List<String> Q = new ArrayList<>();
    private static List<String> F = new ArrayList<>();
    private static String Q0;

    public static void main(String[] args) {
        System.out.println("\n\t\t\t ~~MENU~~");
        HashMap<String, List<String>> mapGrammar = new HashMap<>();
        HashMap<List<String>, String> mapAutomaton = new HashMap<>();
        Integer option;
        while (true) {
            menu();
            option = readOption();
            if (option == 1) {
                reInit();
                mapGrammar = readGrammarFile("Grammar.txt");
                System.out.println("\n\t\tDone !");
            } else if (option == 2) {
                reInit();
                mapGrammar = readGrammarFromKeyboard();
            } else if (option == 3) {
                printList("G");
                printList("N");
                printList("E");
                printList("P");
                printGrammarTerminals(mapGrammar);
            } else if (option == 4) {
                Boolean sem = verifyGrammar(mapGrammar);
                if (sem)
                    System.out.println("\n\t\t\tRegular grammar.");
                else System.out.println("\n\t\t\tNon-regular grammar.");
            } else if (option == 5) {
                reInit();
                mapAutomaton = readAutomatonFromFile("Automaton.txt");
                System.out.println("\n\t\tDone !");
            } else if (option == 6) {
                reInit();
                mapAutomaton = readAutomatonFromKeyboard();
            } else if (option == 7) {
                printList("Q");
                printList("E");
                printList("F");
                printList("Q0");
                printTransactions(mapAutomaton);
            } else if (option == 8) {
                mapAutomaton = grammarIntoAutomata(mapGrammar);
                printList("Q");
                printList("E");
                printList("F");
                printList("Q0");
                printTransactions(mapAutomaton);
            } else if (option == 9) {
                //TODO
                break;
            } else if (option == 0)
                break;
        }
    }

    private static void reInit() {
        G = new ArrayList<>();
        N = new ArrayList<>();
        E = new ArrayList<>();
        P = new ArrayList<>();
        F = new ArrayList<>();
        Q = new ArrayList<>();
        Q0 = "";
    }

    private static void menu() {
        System.out.println("\n\t\t1. Read grammar from file.");
        System.out.println("\t\t2. Read grammar from keyboard.");
        System.out.println("\t\t3. Print grammar.");
        System.out.println("\t\t4. Verify if grammar is regular or not.");
        System.out.println("\t\t5. Read automaton from file.");
        System.out.println("\t\t6. Read automaton from keyboard.");
        System.out.println("\t\t7. Print automaton.");
        System.out.println("\t\t8. Grammar to Automaton.");
        System.out.println("\t\t9. Automaton to Grammar.");
        System.out.println("\t\t0. Exit.");
    }

    private static HashMap<List<String>, String> grammarIntoAutomata(HashMap<String, List<String>> grammar) {
        HashMap<List<String>, String> automata = new HashMap<>();
        List<String> myQ = new ArrayList<>();
        N.remove("S");
        for (Integer pos = 0; pos < N.size(); pos++)
            myQ.add("q" + String.valueOf(pos));

        List<String> myE = new ArrayList<>();
        for (Integer pos = 0; pos < E.size(); pos++)
            myE.add(String.valueOf(pos));

        Iterator it = grammar.entrySet().iterator();
        while (it.hasNext()) {
            List<String> automataList = new ArrayList<>();
            Map.Entry grammarPair = (Map.Entry) it.next();
            List<String> grammarPairList = (List) grammarPair.getValue();
            for (Integer i = 0; i < grammarPairList.size(); i++) {
                String goingInQ = "";
                if (E.contains(grammarPairList.get(i))) {
                    Integer elemPos = E.indexOf(grammarPairList.get(i));
                    String whichIndex = myE.get(elemPos);
                    Integer keyPos = N.indexOf((String) grammarPair.getKey());
                    String fromWhichQ = myQ.get(keyPos);
                    automataList.add(fromWhichQ);
                    automataList.add(whichIndex);
                    goingInQ = fromWhichQ;
                } else if (!grammarPairList.get(i).equals("S")) {
                    String value = grammarPairList.get(i);
                    if (value.length() == 2) {
                        String first = String.valueOf(value.charAt(0));
                        String second = String.valueOf(value.charAt(1));
                        if (E.contains(first) && N.contains(second)) {
                            Integer elemPos = E.indexOf(first);
                            String whichIndex = myE.get(elemPos);
                            Integer keyPos = N.indexOf((String) grammarPair.getKey());
                            String fromWhichQ = myQ.get(keyPos);
                            automataList.add(fromWhichQ);
                            automataList.add(whichIndex);
                            Integer elemPos2 = N.indexOf(second);
                            goingInQ = myQ.get(elemPos2);
                        }
                    }
                }
                if (automataList.size() != 0)
                    automata.put(automataList, goingInQ);
            }
        }
        Q = myQ;
        E = myE;
        F = new ArrayList<>();
        F.add(Q.get(Q.size() - 1));
        Q0 = Q.get(0);
        return automata;
    }

    private static HashMap<String, List<String>> readGrammarFromKeyboard() {
        System.out.print("\n\t\tG = {N, E, P, S}");
        G.add("N");
        G.add("E");
        G.add("P");
        G.add("S");

        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tN = S, ");
        N.add("S");
        Scanner scanner = new Scanner(System.in);
        String myN = scanner.nextLine();
        myN = myN.replaceAll("( )+", "");
        String[] listN = myN.split(",");
        for (Integer number = 0; number < listN.length; number++)
            N.add(listN[number]);

        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tE = ");
        scanner = new Scanner(System.in);
        String myE = scanner.nextLine();
        myE = myE.replaceAll("( )+", "");
        String[] listE = myE.split(",");
        for (Integer number = 0; number < listE.length; number++)
            E.add(listE[number]);


        System.out.print("\n\t\tP = ");
        scanner = new Scanner(System.in);
        String myP = scanner.nextLine();
        P.add(myP);

        System.out.print("\n\t\tInsert '0' when you finished to add to that specific component\n");
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> list;

        for (Integer pos = 0; pos < N.size(); pos++) {
            list = new ArrayList<>();
            while (true) {
                System.out.print("\t\t\t" + N.get(pos) + " -> ");
                Scanner reader = new Scanner(System.in);
                String stuff = reader.nextLine();
                if (stuff.equals("0"))
                    break;
                list.add(stuff);
            }
            map.put(N.get(pos), list);
        }
        return map;
    }

    private static HashMap<List<String>, String> readAutomatonFromKeyboard() {
        System.out.println("\n\t\tNote: Separate with a coma all the values you give.");
        System.out.print("\t\tInsert '0' when you finished to add to that specific component\n");
        System.out.print("\n\t\tQ = ");
        Scanner scanner = new Scanner(System.in);
        String myQ = scanner.nextLine();
        myQ = myQ.replaceAll("( )+", "");
        String[] listQ = myQ.split(",");
        for (Integer number = 0; number < listQ.length; number++)
            Q.add(listQ[number]);

        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tE = ");
        scanner = new Scanner(System.in);
        String myE = scanner.nextLine();
        myE = myE.replaceAll("( )+", "");
        String[] listE = myE.split(",");
        for (Integer number = 0; number < listE.length; number++)
            E.add(listE[number]);

        System.out.print("\n\t\tNote: Separate with a coma all the values you give.\n\t\tF = ");
        String myF = scanner.nextLine();
        myF = myF.replaceAll("( )+", "");
        String[] listF = myF.split(",");
        for (Integer number = 0; number < listF.length; number++)
            F.add(listF[number]);

        System.out.print("\n\t\tQ0 = ");
        Q0 = scanner.nextLine();

        System.out.println("\n\t\tFormat of transaction: T(a, digit) = b. Input equivalent -> a - digit - b");
        System.out.print("\t\tInsert '0' when you finished to add to that specific component\n");
        HashMap<List<String>, String> map = new HashMap<>();
        while (true) {
            List<String> list = new ArrayList<>();
            System.out.print("\t\tT -> ");
            String myT = scanner.nextLine();
            if (myT.equals("0"))
                break;
            myT = myT.replaceAll("( )+", "");
            String[] lineT = myT.split("-");
            list.add(lineT[0]);
            list.add(lineT[1]);
            map.put(list, lineT[2]);
        }
        return map;
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
                if (rightSide.get(1).contains("S"))
                    return false;
                if (rightSide.get(1).length() == 2) {
                    if (rightSide.get(1).charAt(0) < 97 || rightSide.get(1).charAt(0) > 122)
                        return false;
                    if (!E.contains(String.valueOf(rightSide.get(1).charAt(0))))
                        return false;
                    if (rightSide.get(1).charAt(1) < 65 || rightSide.get(1).charAt(1) > 90)
                        return false;
                    if (!N.contains(String.valueOf(rightSide.get(1).charAt(1))))
                        return false;
                } else if (rightSide.get(1).length() == 1) {
                    if (!E.contains(rightSide.get(1)))
                        return false;
                    if (N.contains(rightSide.get(1)))
                        return false;
                }
            } else if (rightSide.size() == 1) {
                if (rightSide.get(0).equals("S") && !pair.getKey().equals("S"))
                    return false;
                if (!E.contains(rightSide.get(0)) && !pair.getKey().equals("S"))
                    return false;
            }
        }
        return true;
    }

    private static Integer readOption() {
        System.out.print("\t\tOption: ");
        Scanner scanner = new Scanner(System.in);
        Integer option = Integer.valueOf(scanner.nextLine());
        return option;
    }

    private static HashMap<String, List<String>> readGrammarFile(String fileName) {
        HashMap<String, List<String>> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            for (Integer i = 1; i <= 3; i++) {
                line = br.readLine();
                if (line.contains("G"))
                    addToList("G", line);
                else if (line.contains("N"))
                    addToList("N", line);
                else if (line.contains("E"))
                    addToList("E", line);
            }
            line = br.readLine();
            line = line.replaceAll("( )+", "");
            String[] lineSplit = line.split("->");
            P.add(lineSplit[1]);
            for (Integer pos = 0; pos < N.size(); pos++)
                result.put(N.get(pos), new ArrayList<>());
            line = br.readLine();
            while (line != null) {
                line = line.replaceAll("( )+", "");
                lineSplit = line.split("->");
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

        } catch (Exception e) {
            System.out.println("File error!");
        }
        return result;
    }

    private static HashMap<List<String>, String> readAutomatonFromFile(String fileName) {
        HashMap<List<String>, String> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            for (int i = 1; i <= 3; i++) {
                line = br.readLine();
                if (line.contains("Q"))
                    putInMyAutomatonLists("Q", line);
                else if (line.contains("E"))
                    putInMyAutomatonLists("E", line);
                else if (line.contains("F"))
                    putInMyAutomatonLists("F", line);
            }

            line = br.readLine();
            line = line.replaceAll("( )+", "");
            String[] lineSplit = line.split("=");

            Q0 = lineSplit[1];

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

    private static void printGrammarTerminals(HashMap<String, List<String>> map) {
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

    private static void printTransactions(HashMap<List<String>, String> map) {
        for (List<String> t : map.keySet()) {
            List<String> key = t;
            String value = map.get(key).toString();
            System.out.println("\t\t\tT(" + key.get(0) + ", " + key.get(1) + ") = " + value);
        }
    }

    private static void printList(String character) {
        if (character.equals("G")) {
            if (listHasItems(G)) {
                String string = "\t\t\tG = {";
                Integer i;
                for (i = 0; i < G.size() - 1; i++)
                    string += G.get(i) + ", ";
                string += G.get(i) + "}";
                System.out.println(string);
            }
        } else if (character.equals("Q")) {
            if (listHasItems(Q)) {
                String string = "\t\t\tQ = {";
                Integer i;
                for (i = 0; i < Q.size() - 1; i++)
                    string += Q.get(i) + ", ";
                string += Q.get(i) + "}";
                System.out.println(string);
            }
        } else if (character.equals("N")) {
            if (listHasItems(N)) {
                String string = "\t\t\tN = {";
                Integer i;
                for (i = 0; i < N.size() - 1; i++)
                    string += N.get(i) + ", ";
                string += N.get(i) + "}";
                System.out.println(string);
            }
        } else if (character.equals("E")) {
            if (listHasItems(E)) {
                String string = "\t\t\tE = {";
                Integer i;
                for (i = 0; i < E.size() - 1; i++)
                    string += E.get(i) + ", ";
                string += E.get(i) + "}";
                System.out.println(string);
            }
        } else if (character.equals("P")) {
            if (listHasItems(P)) {
                String string = "\t\t\tP -> ";
                Integer i;
                for (i = 0; i < P.size() - 1; i++)
                    string += P.get(i) + ", ";
                string += P.get(i);
                System.out.println(string);
            }
        } else if (character.equals("F")) {
            if (listHasItems(F)) {
                String string = "\t\t\tF = {";
                Integer i;
                for (i = 0; i < F.size() - 1; i++)
                    string += F.get(i) + ", ";
                string += F.get(i) + "}";
                System.out.println(string);
            }
        } else if (character.equals("Q0")) {
            if (Q0 != null)
                System.out.println("\t\t\tQ0 = " + Q0);
        }
    }

    private static boolean listHasItems(List<String> list) {
        if (list.size() > 0)
            return true;
        return false;
    }

    private static void addToList(String character, String line) {
        int start = line.indexOf("{") + 1;
        int end = line.indexOf("}");
        line = line.substring(start, end).replaceAll("( )+", "");
        String[] elements = line.split(",");
        for (int pos = 0; pos < elements.length; pos++) {
            if (character.equals("G"))
                G.add(elements[pos]);
            else if (character.equals("N"))
                N.add(elements[pos]);
            else if (character.equals("E"))
                E.add(elements[pos]);
        }
    }

    private static void putInMyAutomatonLists(String string, String line) {
        int start = line.indexOf("{") + 1;
        int end = line.indexOf("}");
        String[] elements = line.substring(start, end).replaceAll("( )+", "").split(",");
        for (int pos = 0; pos < elements.length; pos++) {
            if (string.equals("Q"))
                Q.add(elements[pos]);
            else if (string.equals("E"))
                E.add(elements[pos]);
            else if (string.equals("F"))
                F.add(elements[pos]);
        }
    }
}