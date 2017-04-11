package parser.symbol;

import parser.Utils;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegstotsky on 09.04.17.
 */
public class SymTable {
    public HashMap<String, Symbol> vars;
    public HashMap<String, SymType> types;

    public SymTable() {
        this.vars = new HashMap<>();
        this.types = new HashMap<>();
        this.types.put("integer", new SymTypeInteger());
        this.types.put("float", new SymTypeFloat());
        this.types.put("boolean", new SymTypeBoolean());
    }

    public void addSymbol(String name, Symbol symbol) {
        this.vars.put(name, symbol);
    }

    public void addType(String name, SymType type) throws Exception {
        if (types.containsKey(name)) {
            throw new Exception("");
        }
        this.types.put(name, type);
    }

    public void addSymbols(ArrayList<Token> names, SymType type) throws Exception {
        for (Token name : names) {
            if (this.types.get(name) != null) {
                throw new Exception(String.format("Type %s is already declared",
                        name));
            }
            this.types.put(name.text, type);
        }
    }

    public SymType getType(String name) {
        return types.get(name);
    }


    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("SYM TABLE: VARS");
        for (Map.Entry<String, Symbol> entry : this.vars.entrySet()) {
            entry.getValue().print(depth+1);
        }
        Utils.printIndent(depth);
        System.out.println("SYM TABLE: TYPES");
        for (Map.Entry<String, SymType> entry : this.types.entrySet()) {
            entry.getValue().print(depth+1);
        }
        Utils.printIndent(depth);
        System.out.println("END");
    }
}
