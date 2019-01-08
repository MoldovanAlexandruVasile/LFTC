import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegularGrammar {
    private static List<String> nonTerminals = new ArrayList<>();
    private static List<String> terminals = new ArrayList<>();
    private static List<Production> productions = new ArrayList<>();
    private static String startingSymbol;
    private static final String fileName = "RG.txt";
    private static String w;

    RegularGrammar() {
        readGrammar();
    }

    private void readGrammar() {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);

            String sCurrentLine = br.readLine();
            nonTerminals.addAll(Arrays.asList(sCurrentLine.split(",")));

            sCurrentLine = br.readLine();
            terminals.addAll(Arrays.asList(sCurrentLine.split(",")));

            startingSymbol = br.readLine();
            w = br.readLine();

            while ((sCurrentLine = br.readLine()) != null) {
                String[] prod = sCurrentLine.split(":");
                Production production = findProduction(prod[0]);
                if (production == null) {
                    production = new Production(prod[0]);
                    productions.add(production);
                }
                for (String item : prod[1].split("\\|")) {
                    production.setRHS(item);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public Production findProduction(String lhs) {
        for (Production prod : productions) {
            if (prod.getLHS().equals(lhs))
                return prod;
        }
        return null;
    }

    public String getNonTerminalsToString() {
        String s = "\t\t\tN = {" + nonTerminals.get(0);
        for (int index = 1; index < nonTerminals.size(); index++) {
            s += ", " + nonTerminals.get(index);
        }
        s += "}\n";
        return s;
    }

    public String getTerminalsToString() {
        String s = "\t\t\tE = {" + terminals.get(0);
        for (int index = 1; index < terminals.size(); index++) {
            s += ", " + terminals.get(index);
        }
        s += "}\n";
        return s;
    }

    public String getProductionsToString() {
        String s = "";
        for (Production prod : productions) {
            s += prod.toString() + "\n";
        }
        return s;
    }

    public String getProductionToString(String lhs) {
        Production production = findProduction(lhs);
        if (production == null) {
            return "\tNo productions for the given symbol\n";
        } else {
            return production.toString() + "\n";
        }
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminal(String nonTerminals) {
        this.nonTerminals.add(nonTerminals);
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminal(String terminals) {
        this.terminals.add(terminals);
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProduction(Production productions) {
        this.productions.add(productions);
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public void setStartingSymbol(String startingSymbol) {
        startingSymbol = startingSymbol;
    }

    public String getW() {
        return w;
    }

    public String getWtoString() {
        return "\t\t\tw = " + w;
    }

    public void setW(String w) {
        w = w;
    }
}
