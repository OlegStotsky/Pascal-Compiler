package parser.symbol;

import parser.node.Node;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class SymTypeRecord extends Node {
    ArrayList<Symbol> vars;

    public SymTypeRecord() {
        this.vars = null;
    }

    public SymTypeRecord(ArrayList<Symbol> vars) {
        this.vars = vars;
    }
}
