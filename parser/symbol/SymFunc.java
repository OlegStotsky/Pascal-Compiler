package parser.symbol;

import parser.statement.StmtBlock;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymFunc extends SymProc {
    public SymType returnType;


    public SymFunc() {
        this.returnType = null;
    }

    public SymFunc(String name, SymTable localTable, StmtBlock stmt, SymType returnType) {
        super(name, localTable, stmt);
        this.returnType = returnType;
    }
}
