package parser.node;

import parser.Utils;

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

    public void print(int depth) throws Exception {
        left.print(depth+1);
        Utils.printIndent(depth);
        System.out.println(".");
        right.print(depth+1);
    }
}
