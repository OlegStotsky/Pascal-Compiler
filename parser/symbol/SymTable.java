package parser.symbol;

import parser.Utils;
import tokenizer.Token;
import tokenizer.TokenTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegstotsky on 09.04.17.
 */
public class GlobalSymTable extends SymTable {
    public HashMap<String, Symbol> symbols;
    public HashMap<TokenTypes.TokenType, SymType> tokToType;

    public GlobalSymTable() {//prelude predefined
        this.symbols = new HashMap<>();
        this.symbols.put("integer", SymTypeInteger.getInstance());
        this.symbols.put("float", SymTypeFloat.getInstance());
        this.symbols.put("boolean", SymTypeBoolean.getInstance());
    }

    public void addSymbol(String name, Symbol sym) throws Exception {
        if (this.symbols.containsKey(name)) {
            throw new Exception("Trying to add symbol that already exists");
        }
        this.symbols.put(name, sym);
    }


    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN GLOBAL SYM TABLE");
        Utils.printIndent(depth+1);
        System.out.println("BEGIN GLOBAL SYM TABLE VARS");
        for (Map.Entry<String, Symbol> entry : this.vars.entrySet()) {
            entry.getValue().print(depth+2);
        }
        Utils.printIndent(depth+1);
        System.out.println("END GLOBAL SYM TABLE VARS");
        Utils.printIndent(depth+1);
        System.out.println("BEGIN GLOBAL SYM TABLE TYPES");
        for (Map.Entry<String, SymType> entry : this.types.entrySet()) {
            entry.getValue().print(depth+2);
        }
        Utils.printIndent(depth+1);
        System.out.println("END GLOBAL SYM TABLE TYPES");
        Utils.printIndent(depth);
        System.out.println("END GLOBAL SYM TABLE");
    }
}
