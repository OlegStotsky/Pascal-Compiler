package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymVar extends Symbol {
    public SymType type;

    public SymVar() {
        this.type = null;
    }

    public SymVar(String name, SymType type) {
        super(name);
        this.type = type;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        if (type instanceof SymTypeArray) {
            type = (SymTypeArray) type;
            System.out.println("VAR" + " " + this.name + " : " + "ARRAY["+ ((SymTypeArray) type).start
                    + "..."
                    + ((SymTypeArray) type).end
                    + "] "
                    + "OF "
                    + ((SymTypeArray) type).elemType.name);
        } else {
            System.out.println("VAR" + " " + this.name + ":" + type.name);
        }
    }
}
