package parser.node;

import parser.Utils;
import parser.symbol.SymType;
import parser.symbol.SymTypeFloat;

/**
 * Created by olegstotsky on 14.04.17.
 */
public class NodeFloatConst extends Node {
    public double val;

    public NodeFloatConst() {
        this.val = 0;
    }

    public NodeFloatConst(double val) {
        this.val = val;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println(this.val);
    }

    public SymType getType() {
        return SymTypeFloat.getInstance();
    }
}
