package parser.symbol;

import parser.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegstotsky on 14.04.17.
 */
public class LocalSymTable extends SymTable {
    public HashMap<String, Symbol> vars;
    public GlobalSymTable globalSymTable;

    public LocalSymTable(GlobalSymTable globalSymTable) {
        this.vars = new HashMap<>();
        this.globalSymTable = globalSymTable;
    }

    public void addVar(String name, Symbol symbol, SymType type) throws Exception {
        if (this.globalSymTable.getType(type.toString()) == null) {
            throw new Exception("");
        }

        this.vars.put(name, symbol);
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("BEGIN LOCAL SYM TABLE");
        Utils.printIndent(depth+1);
        System.out.println("BEGIN LOCAL SYM TABLE VARS");
        for (Map.Entry<String, Symbol> entry : this.vars.entrySet()) {
            entry.getValue().print(depth+2);
        }
        Utils.printIndent(depth+1);
        System.out.println("END LOCAL SYM TABLE VARS");
        Utils.printIndent(depth);
        System.out.println("END LOCAL SYM TABLE");
    }
}
