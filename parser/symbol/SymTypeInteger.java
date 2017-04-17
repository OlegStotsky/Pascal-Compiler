package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeInteger extends SymTypeScalar {
    private static SymTypeInteger instance = new SymTypeInteger();

    private SymTypeInteger() {
        super("integer");
    }

    public static SymTypeInteger getInstance() {
        return instance;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE" + " " + this.name);
    }
}
