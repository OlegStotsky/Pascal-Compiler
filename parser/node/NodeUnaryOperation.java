package parser.Node;

public class NodeUnaryOperation extends Node {
    public String operation;
    public Node value;

    public NodeUnaryOperation() {
        this.operation = null;
        this.value = null;
    }

    public NodeUnaryOperation(String sign, Node value) {
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
