package parser.node;

import parser.Utils;
import parser.symbol.SymType;
import parser.symbol.SymTypeBoolean;

/**
 * Created by olegstotsky on 16.04.17.
 */
public class NodeBoolConst extends Node {
    public Boolean val;

    public NodeBoolConst() {
        this.val = null;
    }

    public NodeBoolConst(Boolean val) {
        this.val = val;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println(val.toString());
    }

    public SymType getType() {
        return SymTypeBoolean.getInstance();
    }
}
