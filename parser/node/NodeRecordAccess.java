package parser.node;

public class NodeRecordAccess extends Node {
    public Node left, right;

    public NodeRecordAccess() {
        this.left = null;
        this.right = null;
    }

    public NodeRecordAccess(Node left, Node right) {
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
