package parser;

public class ArrayAccess extends Node {
    public Node id;
    public Node index;

    public ArrayAccess() {
        this.id = null;
        this.index = null;
    }

    public ArrayAccess(Node name, Node index) {
        this.id = name;
        this.index = index;
    }

    public void print(int depth) {
        id.print(depth+1);
        printIndent(depth);
        System.out.println("[]");
        index.print(depth+1);
    }
}
