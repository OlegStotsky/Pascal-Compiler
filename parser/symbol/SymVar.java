package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymVar extends Symbol {
    public Symbol type;

    public SymVar() {
        this.type = null;
    }

    public SymVar(String name, Symbol type) {
        super(name);
        this.type = type;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("VAR");
        Utils.printIndent(depth+1);
        System.out.println("NAME : " + this.name);
        type.print(depth+1);
    }

    public Symbol getType() {
        SymType buf = (SymType)type;
        return buf.getType();
    }
}
