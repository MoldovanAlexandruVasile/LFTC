import java.util.List;

public class Table {
    private String state;
    private Integer i;
    private List<String> workingStack;
    private String inputStack;
    private String operation;

    Table(String state, Integer i, List<String> workingStack, String inputStack, String operation) {
        this.state = state;
        this.i = i;
        this.workingStack = workingStack;
        this.inputStack = inputStack;
        this.operation = operation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public List<String> getWorkingStack() {
        return workingStack;
    }

    public void setWorkingStack(List<String> workingStack) {
        this.workingStack = workingStack;
    }

    public String getInputStack() {
        return inputStack;
    }

    public void setInputStack(String inputStack) {
        this.inputStack = inputStack;
    }

    public Table(String state, Integer i, List<String> workingStack, String inputStack) {
        this.state = state;
        this.i = i;
        this.workingStack = workingStack;
        this.inputStack = inputStack;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "Table{"
                 + state +
                ", " + i +
                ", " + workingStack +
                ", " + inputStack +
                ", " + operation +
                '}';
    }
}
