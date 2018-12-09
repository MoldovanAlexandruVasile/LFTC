package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class GrammarUtils {

    public static List<String> G, NTerminals, ETerminals;
    public static HashMap<String, List<String>> productions = new HashMap<>();
    public static String startingSymbol;

    public static void initGrammar() {
        G = new ArrayList<>();
        NTerminals = new ArrayList<>();
        ETerminals = new ArrayList<>();
        productions = new HashMap<>();
        startingSymbol = "";
    }

    public static HashMap<String, List<String>> readGrammarFromKeyboard() {
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

    public static void printList(String character) {
        if (character.equals("G")) {
            String string = "\t\t\tG = {";
            Integer pos;
            for (pos = 0; pos < G.size() - 1; pos++)
                string += G.get(pos) + ", ";
            string += G.get(pos) + "}";
            System.out.println(string);
        } else if (character.equals("NTerminals")) {
            String string = "\t\t\tN = {";
            Integer pos;
            for (pos = 0; pos < NTerminals.size() - 1; pos++)
                string += NTerminals.get(pos) + ", ";
            string += NTerminals.get(pos) + "}";
            System.out.println(string);
        } else if (character.equals("ETerminals")) {
            String string = "\t\t\tE = {";
            Integer pos;
            for (pos = 0; pos < ETerminals.size() - 1; pos++)
                string += ETerminals.get(pos) + ", ";
            string += ETerminals.get(pos) + "}";
            System.out.println(string);
        }
    }

    public static HashMap<String, List<String>> readGrammarFromFile() {
        HashMap<String, List<String>> result = new HashMap<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(new File("Grammar.txt")));
            for (int pos = 1; pos <= 3; pos++) {
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

    public static void printTerminals(HashMap<String, List<String>> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<String> values = (List) pair.getValue();
            String valuesS = "";
            Integer pos;
            for (pos = 0; pos < values.size() - 1; pos++) {
                valuesS += values.get(pos) + " | ";
            }
            valuesS += values.get(pos);
            System.out.println("\t\t\t" + pair.getKey() + " -> " + valuesS);
        }
    }
}
