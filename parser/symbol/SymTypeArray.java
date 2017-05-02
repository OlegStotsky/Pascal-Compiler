package parser.symbol;

import parser.Utils;
import parser.node.Node;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeArray extends SymType {
    public Symbol elemType;
    public Node start;
    public Node end;

    public SymTypeArray() {
        this.elemType = null;
        this.start = null;
        this.end = null;
    }

    public SymTypeArray(String name, Symbol elemType, Node start, Node end) {
        super(name);
        this.elemType = elemType;
        this.start = start;
        this.end = end;
    }

    public SymTypeArray(Symbol elemType, Node start, Node end) {
        super("");
        this.elemType = elemType;
        this.start = start;
        this.end = end;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        if (name.equals("")) {
            System.out.println(("TYPE : ARRAY"));
        } else {
            System.out.println(String.format("TYPE %s : ARRAY", this.name));
        }
        Utils.printIndent(depth+1);
        System.out.println("START INDEX");
        this.start.print(depth+2);
        Utils.printIndent(depth+1);
        System.out.println("END INDEX");
        this.end.print(depth + 2);
        Utils.printIndent(depth+1);
        System.out.println("ELEM TYPE");
        this.elemType.print(depth+2);
    }

    public Symbol getType() {
        return this;
    }
}