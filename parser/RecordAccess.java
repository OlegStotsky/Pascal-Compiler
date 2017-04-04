package parser;

public class RecordAccess extends Node {
    public Node left, right;

    public RecordAccess() {
        this.left = null;
        this.right = null;
    }

    public RecordAccess(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public void print(int depth) {
        left.print(depth+1);
        printIndent(depth);
        System.out.println(".");
        right.print(depth+1);
    }
}
