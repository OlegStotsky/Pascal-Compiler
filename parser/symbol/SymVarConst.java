package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymVarConst extends SymVar {
    public SymVarConst(String name, Symbol type) {
        super(name, type);
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("CONST VAR");
        Utils.printIndent(depth+1);
        System.out.println("NAME : " + this.name);
        type.print(depth+1);
    }
}
