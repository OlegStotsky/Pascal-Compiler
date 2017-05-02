package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeAlias extends SymType {
    public Symbol refType;

    public SymTypeAlias() {
        super("");
        this.refType = null;
    }

    public SymTypeAlias(String name, Symbol refType) {
        super(name);
        this.refType = refType;
    }

    public SymTypeAlias(Symbol refType) {
        super("");
        this.refType = refType;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        if (this.name.equals("")) {
            System.out.println("TYPE : ALIAS TO");
        } else {
            System.out.println(String.format("TYPE %s : ALIAS TO", this.name));
        }
        this.refType.print(depth+1);
    }

    public Symbol getType() {
       SymType buf = (SymType)refType;
       return buf.getType();
    }
}
