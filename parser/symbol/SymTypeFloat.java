package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeFloat extends SymTypeScalar {
    public SymTypeFloat() {
        super("float");
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE" + " " + this.name);
    }
}
