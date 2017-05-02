package parser.node;

import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.Symbol;

public abstract class Node {
    public void print(int depth) throws Exception {

    }

    public String toString() {
        return "";
    }

    public Symbol getType(SymTable symTable) throws Exception {
        return null;
    }
}

