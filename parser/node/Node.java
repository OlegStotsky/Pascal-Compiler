package parser.node;

import parser.symbol.SymType;

public abstract class Node {
    public void print(int depth) {

    }

    public String toString() {
        return "";
    }

    public SymType getType() throws Exception {
        return null;
    }
}

