package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 09.04.17.
 */
public class SymTypeBoolean extends SymType {
    public SymTypeBoolean() {
        super("boolean");
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE" + " " + this.name);
    }
}
