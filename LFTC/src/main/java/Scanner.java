import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Scanner {
    private Integer lineNumber = 1;
    private Integer lastIdentifierPos = 0;
    private Integer lastConstantPos = 0;
    private File file;
    private File PIFFile;
    private File identifiersTableFile;
    private File constantsTableFile;
    private List<HashMap<String, Pair<Integer, Integer>>> PIF = new ArrayList<>();
    private HashMap<String, Integer> identifiersTable = new HashMap<>();
    private HashMap<String, Integer> constantsTable = new HashMap<>();
    private HashMap<String, Integer> tableTxt = new HashMap<>();

    Scanner(String fileName) {
        try {
            file = new File(fileName);
            PIFFile = new File("PIF.txt");
            identifiersTableFile = new File("IdentifiersTable.txt");
            constantsTableFile = new File("ConstantsTable.txt");
            PIFFile.createNewFile();
            identifiersTableFile.createNewFile();
            constantsTableFile.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        populateTable();
    }

    private void populateTable() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("IdTable.txt")));
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
                if (!checkLineContainsIfWhileForStmt(line)) {
                    System.out.println("Syntax error at line number " + lineNumber);
                    break;
                }
                if (line.contains("}") && line.length() > 1) {
                    System.out.println("Syntax error at line number " + lineNumber);
                    break;
                }
                if (line.contains("VAR") && line.charAt(line.length() - 1) != '{') {
                    System.out.println("Syntax error at line number " + lineNumber);
                    break;
                }
                if (line.charAt(line.length() - 1) != ';' &&
                        !(line.contains("{") || line.contains("}") || line.contains("(") || line.contains(")"))) {
                    System.out.println("Syntax error at line number " + lineNumber);
                    break;
                }
                if ((line.contains("if") || line.contains("while") || line.contains("for")) &&
                        line.charAt(line.length() - 1) != ')') {
                    System.out.println("Syntax error at line number " + lineNumber);
                    break;
                }
                String[] lineSplit = line.split(" ");
                for (Integer pos = 0; pos < lineSplit.length; pos++) {
                    String elem = lineSplit[pos].replaceAll("[ ]+|\t|\n|\r", "");
                    if (elem.length() != 1 && !elem.equals("")) {
                        List<String> elements = getElementsToBeAdded(elem);
                        if (elements.size() == 0)
                            System.out.println("Syntax error at line number " + lineNumber);
                        else
                            for (Integer pos2 = 0; pos2 < elements.size(); pos2++) {
                                String element = elements.get(pos2);
                                addElementIntoTable(element);
                            }
                    } else addElementIntoTable(elem);
                }
                lineNumber++;
                line = br.readLine();
            }
            writeTables();
        } catch (IOException e) {
            System.out.println("Reader error!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addElementIntoTable(String element) {
        if (!element.equals("")) {
            if (element.equals("\\("))
                element = String.valueOf('(');
            else if (element.equals("\\)"))
                element = String.valueOf(')');
            Integer nature = checkElementNature(element);
            Integer elementP;
            HashMap<String, Pair<Integer, Integer>> firstMap = new HashMap<>();
            if (nature == -1) {
                Integer elementPos = tableTxt.get(element);
                firstMap.put(element, new Pair<>(elementPos, nature));
                PIF.add(firstMap);
            } else if (nature == 1) {
                if (!constantsTable.containsKey(element)) {
                    constantsTable.put(element, lastConstantPos);
                    lastConstantPos++;
                }
                elementP = getElementPos(constantsTable, element);
                firstMap.put(element, new Pair<>(elementP, nature));
                PIF.add(firstMap);
            } else if (nature == 0 && element.length() <= 8) {
                if (!identifiersTable.containsKey(element)) {
                    identifiersTable.put(element, lastIdentifierPos);
                    lastIdentifierPos++;
                }
                elementP = getElementPos(identifiersTable, element);
                firstMap.put(element, new Pair<>(elementP, nature));
                PIF.add(firstMap);
            } else if (!element.equals(""))
                System.out.println("Syntax error at line number " + lineNumber);
        }
    }

    private List<String> getElementsToBeAdded(String element) {
        String extraChar = checkExtraCharsExistence(element);
        List<String> returnList = new ArrayList<>();
        if (!extraChar.equals("") && String.valueOf(element.charAt(0)).equals(extraChar)) {
            returnList.add(extraChar);
            element = element.replaceAll(extraChar, "");
            if (extraChar.equals(";") || extraChar.equals("\\}") || extraChar.equals("\\{"))
                return new ArrayList<>();
            if (tableTxt.containsKey(element))
                returnList.add(element);
            else return new ArrayList<>();
        } else if (!extraChar.equals("") && String.valueOf(element.length() - 1).equals(extraChar)) {
            element = element.replaceAll(extraChar, "");
            if (tableTxt.containsKey(element)) {
                returnList.add(element);
                returnList.add(extraChar);
            } else return new ArrayList<>();
        } else if (!extraChar.equals("")) {
            String[] lists = element.split(extraChar);
            Integer extraToBeAdded = lists.length - 1;
            for (Integer elem = 0; elem < lists.length; elem++) {
                returnList.add(lists[elem]);
                if (extraToBeAdded != 0) {
                    returnList.add(extraChar);
                    extraToBeAdded--;
                }
            }
        } else if (extraChar.equals("")) {
            returnList.add(element);
        }
        return returnList;
    }

    private Integer getElementPos(HashMap<String, Integer> table, String element) {
        Integer pos = 0;
        Iterator it = table.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey().equals(element))
                return pos;
            pos++;
        }
        return -1;
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
            Iterator it = identifiersTable.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                writer.write("'" + pair.getKey() + "'" + " " + pair.getValue() + "\n");
            }

            BufferedWriter writer2 = new BufferedWriter(new FileWriter(constantsTableFile, false));
            it = constantsTable.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                writer2.write("'" + pair.getKey() + "'" + " " + pair.getValue() + "\n");
            }

            BufferedWriter writer1 = new BufferedWriter(new FileWriter(PIFFile, false));
            for (Integer i = 0; i < PIF.size(); i++) {
                HashMap<String, Pair<Integer, Integer>> map = PIF.get(i);
                String key = map.entrySet().iterator().next().getKey();
                Pair<Integer, Integer> value = map.entrySet().iterator().next().getValue();
                writer1.write("'" + key + "'" + " " + value.getKey() + " " + value.getValue() + "\n");
            }
            writer.close();
            writer1.close();
            writer2.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkLineContainsIfWhileForStmt(String line) {
        if (line.contains("if")) {
            if (!(line.contains("(") && line.contains(")")))
                return false;
            Integer first = line.indexOf("(");
            Integer second = line.indexOf(")");
            String element = line.substring(first + 1, second);
            element = element.trim();
            element = element.replaceAll("( )+", " ");
            String[] list = element.split(" ");
            if (!list[2].matches("[0-9]+"))
                if (!identifiersTable.containsKey(list[2]))
                    return false;
            if (!identifiersTable.containsKey(list[0]))
                return false;
            if (list.length != 3 || !(list[1].equals("<") || list[1].equals("<=") || list[1].equals("==") ||
                    list[1].equals("!=") || list[1].equals(">") || list[1].equals(">=")))
                return false;

        } else if (line.contains("while")) {
            if (!(line.contains("(") && line.contains(")")))
                return false;
            Integer first = line.indexOf("(");
            Integer second = line.indexOf(")");
            String element = line.substring(first + 1, second);
            element = element.trim();
            element = element.replaceAll("( )+", " ");
            String[] list = element.split(" ");
            if (!list[2].matches("[0-9]+"))
                if (!identifiersTable.containsKey(list[2]))
                    return false;
            if (!identifiersTable.containsKey(list[0]))
                return false;
            if (list.length != 3 || !(list[1].equals("<") || list[1].equals("<=") || list[1].equals("==") ||
                    list[1].equals("!=") || list[1].equals(">") || list[1].equals(">=")))
                return false;

        } else if (line.contains("for")) {
            if (!(line.contains("(") && line.contains(")")))
                return false;
            Integer first = line.indexOf("(");
            Integer second = line.indexOf(")");
            String element = line.substring(first + 1, second);
            element = element.trim();
            element = element.replaceAll("( )+", " ");
            String[] list = element.split(";");
            if (list.length != 3)
                return false;
            String[] param1 = list[0].trim().split(" ");
            String[] param2 = list[1].trim().split(" ");
            String[] param3 = list[2].trim().split(" ");
            if (!identifiersTable.containsKey(param1[0]))
                return false;
            if (param1.length != 3)
                return false;
            if (!param1[1].equals("="))
                return false;
            if (!param1[2].matches("[0-9]+"))
                if (!identifiersTable.containsKey(param1[2]))
                    return false;
            if (param2.length != 3)
                return false;
            if (!param2[0].equals(param1[0]))
                return false;
            if (!(param2[1].equals("<") || param2[1].equals("<=") || param2[1].equals(">") || param2[1].equals(">=")))
                return false;
            if (!param2[2].matches("[0-9]+"))
                if (!identifiersTable.containsKey(param2[2]))
                    return false;
            if (param3.length != 5)
                return false;
            if (!param3[0].equals(param1[0]) || !param3[2].equals(param1[0]))
                return false;
            if (!param3[1].equals("="))
                return false;
            if (!(param3[3].equals("+") && !param3[3].equals("-")))
                return false;
            if (!param3[4].matches("[0-9]+"))
                if (!identifiersTable.containsKey(param3[4]))
                    return false;
        }
        return true;
    }

    private String checkExtraCharsExistence(String element) {
        if (element.contains("{"))
            return "\\{";
        else if (element.contains("}"))
            return "\\}";
        else if (element.contains("-"))
            return "-";
        else if (element.contains("!"))
            return "!";
        else if (element.contains("!="))
            return "!=";
        else if (element.contains("&&"))
            return "&&";
        else if (element.contains(String.valueOf('(')))
            return "\\(";
        else if (element.contains(")"))
            return "\\)";
        else if (element.contains("*"))
            return "*";
        else if (element.contains(","))
            return ",";
        else if (element.contains("/"))
            return "/";
        else if (element.contains(";"))
            return ";";
        else if (element.contains("||"))
            return "||";
        else if (element.contains(String.valueOf('"')))
            return "\"";
        else if (element.contains("+"))
            return "\\+";
        else if (element.contains("<"))
            return "<";
        else if (element.contains("<="))
            return "<=";
        else if (element.contains("="))
            return "=";
        else if (element.contains("=="))
            return "==";
        else if (element.contains(">"))
            return ">";
        else if (element.contains(">="))
            return ">=";
        else return "";
    }
}
