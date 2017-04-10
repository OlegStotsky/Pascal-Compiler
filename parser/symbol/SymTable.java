package parser.Symbol;

import java.util.HashMap;

/**
 * Created by olegstotsky on 09.04.17.
 */
public class SymTable {
    public HashMap<String, Symbol> vars;
    public HashMap<String, SymType> types;

    public SymTable() {
        this.vars = new HashMap<>();
        this.types.put("Integer", new SymTypeInteger());
        this.types.put("Float", new SymTypeFloat());
        this.types.put("Boolean", new SymTypeBoolean());
    }

    public void addSymbol(String name, Symbol symbol) {
        this.vars.put(name, symbol);
    }

    public void addType(String name, SymType type) throws Exception {
        if (types.containsKey(name)) {
            throw new Exception("");
        }
        types.put(name, type);
    }

    public SymType getType(String name) {
        return types.get(name);
    }
}
