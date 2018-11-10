import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {

    private static List<String> G = new ArrayList<>();
    private static List<String> N = new ArrayList<>();
    private static List<String> E = new ArrayList<>();
    private static List<String> P = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("\n\t\t\t ~~MENU~~");
        HashMap<String, List<String>> map = new HashMap<>();
        Integer option;
        while (true) {
            menu();
            option = readOption();
            if (option == 1) {
                reInit();
                map = readFromFile("Grammar.txt");
                System.out.println("\n\t\tDone !");

            } else if (option == 2) {
                reInit();
                map = readFromKeyboard();
            } else if (option == 3) {
                System.out.println("\n\t\tGrammar:");
                printList("G");
                printList("N");
                printList("E");
                printList("P");
                printTerminals(map);
            } else if (option == 4) {
                Boolean sem = verifyGrammar(map);
                if (sem)
                    System.out.println("\n\t\t\tRegular grammar.");
                else System.out.println("\n\t\t\tNonregular grammar.");
            } else if (option == 0)
                break;
        }
    }

    private static void reInit() {
        G = new ArrayList<>();
        N = new ArrayList<>();
        E = new ArrayList<>();
        P = new ArrayList<>();
    }

    private static void menu() {
        System.out.println("\n\t\t1. Read grammar from file.");
        System.out.println("\t\t2. Read grammar from keyboard.");
        System.out.println("\t\t3. Print grammar.");
        System.out.println("\t\t4. Verify grammar grammar.");
        System.out.println("\t\t0. Exit.");
    }

    private static HashMap<String, List<String>> readFromKeyboard() {
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

    private static HashMap<String, List<String>> readFromFile(String fileName) {
        HashMap<String, List<String>> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            for (Integer i = 1; i <= 3; i++) {
                line = br.readLine();
                if (line.contains("G"))
                    addToWantedList("G", line);
                else if (line.contains("N"))
                    addToWantedList("N", line);
                else if (line.contains("E"))
                    addToWantedList("E", line);
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


    private static void printList(String character) {
        if (character.equals("G")) {
            String string = "\t\t\tG = {";
            Integer i;
            for (i = 0; i < G.size() - 1; i++)
                string += G.get(i) + ", ";
            string += G.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("N")) {
            String string = "\t\t\tN = {";
            Integer i;
            for (i = 0; i < N.size() - 1; i++)
                string += N.get(i) + ", ";
            string += N.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("E")) {
            String string = "\t\t\tE = {";
            Integer i;
            for (i = 0; i < E.size() - 1; i++)
                string += E.get(i) + ", ";
            string += E.get(i) + "}";
            System.out.println(string);
        } else if (character.equals("P")) {
            String string = "\t\t\tP -> ";
            Integer i;
            for (i = 0; i < P.size() - 1; i++)
                string += P.get(i) + ", ";
            string += P.get(i);
            System.out.println(string);
        }
    }

    private static void addToWantedList(String character, String line) {
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
}