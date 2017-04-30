package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 09.04.17.
 */
public class SymTypeBoolean extends SymType {
    public static SymTypeBoolean instance = new SymTypeBoolean();

    private SymTypeBoolean() {
        super("boolean");
    }

    public static SymTypeBoolean getInstance() {
        return instance;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("TYPE : " + this.name);
    }
}
