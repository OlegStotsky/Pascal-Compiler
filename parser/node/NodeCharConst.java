package parser.node;

import parser.Utils;
import parser.node.Node;
import parser.symbol.SymType;
import parser.symbol.SymTypeChar;

/**
 * Created by olegstotsky on 14.04.17.
 */
public class NodeCharConst extends Node {
    public char val;

    public NodeCharConst() {
        this.val = 0;
    }

    public NodeCharConst(char val) {
        this.val = val;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println(this.val);
    }

    public SymType getType() {
        return SymTypeChar.getInstance();
    }
}
