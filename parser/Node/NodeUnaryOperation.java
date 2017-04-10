package parser;

public class UnaryOperation extends Node {
    public String operation;
    public Node value;

    public UnaryOperation() {
        this.operation = null;
        this.value = null;
    }

    public UnaryOperation(String sign, Node value) {
        this.operation = sign;
        this.value = value;
    }

    public void print(int depth) {
        if (operation != null) {
            printIndent(depth);
            System.out.println(operation);
        }
        value.print(depth+1);
    }
}
