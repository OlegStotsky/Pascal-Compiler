package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 17.04.17.
 */
public class SymTypeChar extends SymType {
    public static SymTypeChar instance = new SymTypeChar();

    private SymTypeChar() {
        super("char");
    }

    public static SymTypeChar getInstance() {
        return instance;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE : " + this.name);
    }

    public Symbol getType() {
        return instance;
    }
}
