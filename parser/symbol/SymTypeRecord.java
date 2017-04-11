package parser.symbol;

import parser.Utils;
import parser.node.Node;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class SymTypeRecord extends SymType {
    SymTable table;

    public SymTypeRecord() {
        this.table = null;
    }

    public SymTypeRecord(String name, SymTable table) {
        super(name);
        this.table = table;
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println(String.format("TYPE %s : RECORD",
                this.name));
        table.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("END");
    }
}
