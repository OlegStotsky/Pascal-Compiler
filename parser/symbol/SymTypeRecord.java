package parser.symbol;

import parser.node.Node;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class SymTypeRecord extends Node {
    SymTable table;

    public SymTypeRecord() {
        this.table = null;
    }

    public SymTypeRecord(SymTable table) {
        this.table = table;
    }
}
