package parser.symbol;

import parser.Utils;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class SymTypeRecord extends SymType {
    public SymTable symTable;

    public SymTypeRecord() {
        this.symTable = null;
    }

    public SymTypeRecord(String name, SymTable table) {
        super(name);
        this.symTable = table;
    }

    public SymTypeRecord(SymTable table) {
        super("");
        this.symTable = table;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        if (name.equals("")) {
            System.out.println(String.format("BEGIN TYPE : RECORD",
                    this.name));
        } else {
            System.out.println(String.format("TYPE %s : RECORD",
                    this.name));
        }
        symTable.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("END TYPE : RECORD");
    }

    public Symbol getType() {
        return this;
    }
}
