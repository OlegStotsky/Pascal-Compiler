package parser.node;

import parser.Utils;
import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.Symbol;

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

    public void print(int depth) throws Exception {
        if (operation != null) {
            Utils.printIndent(depth);
            System.out.println(operation);
            value.print(depth+1);
        } else {
            value.print(depth);
        }
    }

    public Symbol getType(SymTable symTable) throws Exception {
        return value.getType(symTable);
    }
}
