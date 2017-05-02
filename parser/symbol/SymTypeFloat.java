package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeFloat extends SymTypeScalar {
    static SymTypeFloat instance = new SymTypeFloat();

    private SymTypeFloat() {
        super("float");
    }

    public static SymTypeFloat getInstance() {
        return instance;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE : "+ this.name);
    }

    public Symbol getType() {
        return instance;
    }
}
