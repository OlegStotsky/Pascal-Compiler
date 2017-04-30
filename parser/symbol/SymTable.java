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
public class SymTable {
    public HashMap<String, Symbol> symbols;
    public SymTable parent;

    public SymTable(SymTable parent) {
        this.symbols = new HashMap<>();
        this.parent = parent;
    }

    public void addSymbol(String name, Symbol sym) throws Exception {
        if (this.symbols.containsKey(name)) {
            throw new Exception(String.format("Error : Trying to add symbol '%s' that already exists", name));
        }
        this.symbols.put(name, sym);
    }

    public void exists(String name) {

    }

    public Symbol getSymbol(String name) throws Exception {
    	SymTable curTable = this;
    	while (curTable != null) {
    		Symbol target = curTable.symbols.get(name);
    		if (target == null) {
                curTable = curTable.parent;
            } else {
                return target;
            }
    	}

        throw new Exception(String.format("Coudn't find symbol %s", name));
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN SYMTABLE");
        for (Map.Entry<String, Symbol> entry : this.symbols.entrySet()) {
            entry.getValue().print(depth+1);
        }
        Utils.printIndent(depth);
        System.out.println("END SYMTABLE");
    }
}
