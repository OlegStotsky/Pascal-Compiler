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
//        if (type instanceof SymTypeArray) {
//            type = (SymTypeArray) type;
//            System.out.println("VAR" + " " + this.name + " : " + "ARRAY["+ ((SymTypeArray) type).start
//                    + "..."
//                    + ((SymTypeArray) type).end
//                    + "] "
//                    + "OF "
//                    + ((SymTypeArray) type).elemType.name);
//        } else {
//            System.out.println("VAR" + " " + this.name + ":" + type.name);
//        }
        System.out.println("VAR");
        Utils.printIndent(depth+1);
        System.out.println("NAME : " + this.name);
        type.print(depth+1);
    }
}
