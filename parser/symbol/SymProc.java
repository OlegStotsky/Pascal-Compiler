package parser.symbol;

import parser.Utils;
import parser.statement.Statement;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymProc extends Symbol {
    SymTable localTable;
    Statement stmt;

    public SymProc() {
        this.localTable = null;
        this.stmt = null;
    }

    public SymProc(String name, SymTable localTable, Statement stmt) {
        super(name);
        this.localTable = localTable;
        this.stmt = stmt;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println(String.format("PROCEDURE %s", this.name));
        this.localTable.print(depth+1);
        this.stmt.print(depth+1);
        Utils.printIndent(depth);
        System.out.println(String.format("END PROCEDURE %s", this.name));
    }
}
