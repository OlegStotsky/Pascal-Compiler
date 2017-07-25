package parser.node;

import parser.Utils;
import parser.symbol.SymTable;
import parser.symbol.SymTypeArray;
import parser.symbol.Symbol;

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

    @Override
    public Symbol getType(SymTable symTable) throws Exception {
        return ((SymTypeArray)id.getType(symTable)).elemType;
    }

    public void print(int depth) throws Exception {
        id.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("[]");
        index.print(depth+1);
    }
}
