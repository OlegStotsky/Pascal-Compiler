package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypePointer extends SymType {
    public Symbol refType;

    public SymTypePointer() {
        this.refType = null;
    }

    public SymTypePointer(String name, Symbol refType) {
        super(name);
        this.refType = refType;
    }

    public SymTypePointer(Symbol refType) {
        super("");
        this.refType = refType;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        if (this.name.equals("")) {
            System.out.println("TYPE : POINTER TO");
        } else {
            System.out.println(String.format("TYPE %s : POINTER TO ", this.name));
        }
        refType.print(depth+1);
    }

    public Symbol getType() {
        return this;
    }
}
