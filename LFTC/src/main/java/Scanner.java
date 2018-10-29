import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    private Integer lastIdentifierPos = 0;
    private Integer lastConstantPos = 0;
    private File file;
    private File tableFile;
    private File identifiersTableFile;
    private File constantsTableFile;
    private List<HashMap<String, Pair<Integer, Integer>>> PIF = new ArrayList<>();
    private List<HashMap<String, Integer>> identifiersTable = new ArrayList<>();
    private List<HashMap<String, Integer>> constantsTable = new ArrayList<>();
    private HashMap<String, Integer> tableTxt = new HashMap<>();

    Scanner(String fileName) {
        try {
            file = new File(fileName);
            tableFile = new File("Table.txt");
            identifiersTableFile = new File("IdentifiersTable.txt");
            constantsTableFile = new File("ConstantsTable.txt");
            tableFile.createNewFile();
            identifiersTableFile.createNewFile();
            constantsTableFile.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        populateTable();
    }

    private void populateTable() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("InitialTable.txt")));
            String line = br.readLine();
            while (line != null) {
                String[] pair = line.split(" ");
                tableTxt.put(pair[0], Integer.valueOf(pair[1]));
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("File error!");
        }
    }

    void tokenize() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                String[] lineSplit = line.split(" ");
                for (Integer pos = 0; pos < lineSplit.length; pos++) {
                    String element = lineSplit[pos].replaceAll("'|,|\"|;|[ ]+|[(]|[)]|[{]|[}]|\t|\n|\r", "");
                    Integer nature = checkElementNature(element);
                    HashMap<String, Pair<Integer, Integer>> firstMap = new HashMap<>();
                    HashMap<String, Integer> map = new HashMap<>();
                    if (nature == -1) {
                        Integer elementPos = tableTxt.get(element);
                        firstMap.put(element, new Pair<>(elementPos, nature));
                        PIF.add(firstMap);
                    } else if (nature == 1) {
                        map.put(element, lastConstantPos);
                        constantsTable.add(map);
                        firstMap.put(element, new Pair<>(lastConstantPos, nature));
                        PIF.add(firstMap);
                        lastConstantPos++;
                    } else if (nature == 0 && element.length() <= 8) {
                        map.put(element, lastIdentifierPos);
                        identifiersTable.add(map);
                        firstMap.put(element, new Pair<>(lastIdentifierPos, nature));
                        PIF.add(firstMap);
                        lastIdentifierPos++;
                    }
                }
                line = br.readLine();
            }
            writeTables();
        } catch (IOException e) {
            System.out.println("Reader error!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Integer checkElementNature(String element) {
        if (tableTxt.containsKey(element))
            return -1;
        else if (element.matches("[a-zA-Z_]+"))
            return 0;
        else if (element.matches("[0-9]+"))
            return 1;
        return -2;
    }

    private void writeTables() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(identifiersTableFile, false));
            for (Integer i = 0; i < identifiersTable.size(); i++) {
                HashMap<String, Integer> map = identifiersTable.get(i);
                String key = map.entrySet().iterator().next().getKey();
                Integer value = map.entrySet().iterator().next().getValue();
                writer.write(key + " " + value + "\n");
            }

            BufferedWriter writer2 = new BufferedWriter(new FileWriter(constantsTableFile, false));
            for (Integer i = 0; i < constantsTable.size(); i++) {
                HashMap<String, Integer> map = constantsTable.get(i);
                String key = map.entrySet().iterator().next().getKey();
                Integer value = map.entrySet().iterator().next().getValue();
                writer2.write(key + " " + value + "\n");
            }

            BufferedWriter writer1 = new BufferedWriter(new FileWriter(tableFile, false));
            for (Integer i = 0; i < PIF.size(); i++) {
                HashMap<String, Pair<Integer, Integer>> map = PIF.get(i);
                String key = map.entrySet().iterator().next().getKey();
                Pair<Integer, Integer> value = map.entrySet().iterator().next().getValue();
                writer1.write(key + " " + value.getKey() + " " + value.getValue() + "\n");
            }

            writer.close();
            writer1.close();
            writer2.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
