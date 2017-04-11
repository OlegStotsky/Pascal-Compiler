package parser.node;

import parser.Utils;

public class NodeArrayAccess extends Node {
    public Node id;
    public Node index;

    public NodeArrayAccess() {
        this.id = null;
        this.index = null;
    }

    public NodeArrayAccess(Node name, Node index) {
        this.id = name;
        this.index = index;
    }

    public void print(int depth) {
        id.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("[]");
        index.print(depth+1);
    }
}
