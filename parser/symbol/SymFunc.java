package parser.symbol;

import parser.statement.StmtBlock;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymFunc extends SymProc {
    public Symbol returnType;

    public SymFunc() {
        this.returnType = null;
    }

    public SymFunc(String name, SymTable localTable, StmtBlock stmt, Symbol returnType, ArrayList<SymVar> params) {
        super(name, localTable, stmt, params);
        this.returnType = returnType;
    }
}
