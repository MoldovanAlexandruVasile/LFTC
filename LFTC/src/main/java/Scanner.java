import javafx.util.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Scanner {
    private File file;
    private File outputFile;
    private File tableFile;
    private File symbolTableFile;
    private HashMap<String, Pair<Integer, Integer>> table = new HashMap<>();
    private HashMap<String, Integer> symbolTable = new HashMap<>();
    private HashMap<String, Integer> initialTable = new HashMap<>();

    Scanner(String fileName) {
        try {
            file = new File(fileName);
            outputFile = new File(fileName.replace(".txt", "") + "Output.txt");
            tableFile = new File(fileName.replace(".txt", "") + "Table.txt");
            symbolTableFile = new File(fileName.replace(".txt", "") + "SymbolTable.txt");
            tableFile.createNewFile();
            symbolTableFile.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        populateTable();
    }

    public void populateTable() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("table.txt"));
            String line = br.readLine();
            while (line != null) {
                String[] pair = line.split(" ");
                Integer value = Integer.valueOf(pair[1]);
                initialTable.put(pair[0], value);
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("File error !");
        }
    }

    public void tokenize() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            int lineNr = 0;
            String id = "";
            boolean ok = true;
            while (line != null) {
                lineNr++;
                int len = line.length();
                int i = 0;
                //we add chars to the current identfier as long as the current char is a letter
                while (i < len) {
                    if (line.charAt(i) == ' ' && !id.equals("")) {
                        ok = addToken(id);
                        if (ok == false) {
                            System.out.println("Syntax error at line " + lineNr + " and column " + i);
                        }
                        id = "";
                        i++;
                        continue;
                    }
                    if (i > 0) {
                        if ((line.charAt(i - 1) == '>' ||
                                line.charAt(i - 1) == '=' ||
                                line.charAt(i - 1) == '!' ||
                                line.charAt(i - 1) == '<') && line.charAt(i) == '=') {
                            ok = addToken(line.charAt(i - 1) + "=");
                            id = "";
                            if (ok == false) {
                                System.out.println("Syntax error at line " + lineNr + " and column " + i);
                            }
                            i++;
                            continue;
                        }
                    }
                    if (new String("{};()+-*/*").contains(String.valueOf(line.charAt(i))) || line.charAt(i) == '\n') {
                        ok = addToken(String.valueOf(line.charAt(i)));
                        if (ok == false) {
                            System.out.println("Syntax error at line " + lineNr + " and column " + i);
                            i++;
                            continue;
                        }
                        ok = addToken(id);

                        if (ok == false) {
                            System.out.println("Syntax error at line " + lineNr + " and column " + i);
                        }
                        id = "";
                        i++;
                        continue;
                    }
                    if (line.charAt(i) != ' ') id += String.valueOf(line.charAt(i));
                    i++;
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

    public boolean isChar(char x) {
        return ((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z'));
    }

    public boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    public boolean addToken(String token) {
        if (initialTable.containsKey(token)) {
            table.put(token, new Pair(initialTable.get(token), -1));
            return true;
        } else {
            return addIdentifierOrConstant(token);
        }
    }

    private int addSymbol(String token) {
        if (symbolTable.containsKey(token)) {
            return symbolTable.get(token);
        } else {
            symbolTable.put(token, symbolTable.size() + 1);
            return -1;
        }
    }

    private boolean addIdentifierOrConstant(String token) {
        int ok = checkToken(token);
        if (ok == -1) return false;
        if (symbolTable.containsKey(token)) {
            table.put(token, new Pair(ok, symbolTable.get(token)));
        } else {
            symbolTable.put(token, symbolTable.size() + 1);
            table.put(token, new Pair(ok, symbolTable.get(token)));
        }
        return true;
    }

    private int randNotTaken(HashMap<String, Integer> table) {
        int r = ThreadLocalRandom.current().nextInt(1, 10000);
        while (table.containsValue(r)) {
            r = ThreadLocalRandom.current().nextInt(1, 10000);
        }
        return r;
    }

    private void writeTables() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(symbolTableFile, true));
            for (Map.Entry<String, Integer> e : symbolTable.entrySet()) {
                String key = e.getKey();
                int value = e.getValue();
                writer.write(key + " " + value);
                writer.newLine();
            }
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(tableFile, true));
            for (Map.Entry<String, Pair<Integer, Integer>> e : table.entrySet()) {
                String key = e.getKey();
                Pair<Integer, Integer> value = e.getValue();
                writer1.write(key + " " + value.getKey() + " " + value.getValue() + "\n");
                writer1.newLine();
            }
            writer.close();
            writer1.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int checkToken(String token) {
        if (token.length() > 8) return -1;
        if (token.matches("^[0-9]*.[0-9].$")) return 1;
        if (token.matches("^[a-zA-Z_$][a-zA-Z_$]*$")) return 0;
        return -1;
    }
}
