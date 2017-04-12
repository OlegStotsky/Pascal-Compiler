package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypePointer extends SymType {
    public SymType refType;

    public SymTypePointer() {
        this.refType = null;
    }

    public SymTypePointer(SymType refType) {
        this.refType = refType;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println(String.format("TYPE %s : POINTER TO :" + " " + refType.toString(),
                this.name));
    }
}
