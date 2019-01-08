import java.util.ArrayList;
import java.util.List;

public class Production {
    private static String lhs;
    private static List<String> rhs = new ArrayList<>();

    Production(String lhs) {
        this.lhs = lhs;
    }

    public String getLHS() {
        return this.lhs;
    }

    public List<String> getRHS() {
        return this.rhs;
    }

    public void setLHS(String lhs) {
        this.lhs = lhs;
    }

    public void setRHS(String rhs) {
        this.rhs.add(rhs);
    }

    @Override
    public String toString() {
        return "\t\t\tProduction{" +
                "LHS='" + lhs + '\'' +
                ",RHS = " + rhs +
                '}';
    }
}
