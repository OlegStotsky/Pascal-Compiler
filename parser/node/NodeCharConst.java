package parser.node;

import parser.Utils;
import parser.node.Node;
import parser.symbol.*;

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

    public Symbol getType(SymTable symTable) throws Exception {
        return SymTypeChar.getInstance();
    }
}
