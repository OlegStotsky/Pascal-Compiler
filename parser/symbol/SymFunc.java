package parser.symbol;

import parser.statement.StmtBlock;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymFunc extends SymProc {
    public Symbol returnType;


    public SymFunc() {
        this.returnType = null;
    }

    public SymFunc(String name, SymTable localTable, StmtBlock stmt, Symbol returnType) {
        super(name, localTable, stmt);
        this.returnType = returnType;
    }
}
