package parser.symbol;

import parser.Utils;
import parser.exceptions.*;
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

    public Symbol getSymbol(String name, boolean isRecursive) throws Exception {
        if (!isRecursive) {
            return this.symbols.get(name);
        }

    	SymTable curTable = this;
    	while (curTable != null) {
    		Symbol target = curTable.symbols.get(name);
    		if (target == null) {
                curTable = curTable.parent;
            } else {
                return target;
            }
    	}

        throw new NullSymbolException(name);
    }

    public SymVar getVar(String name, boolean isRecursive) throws Exception {
        Symbol var = this.getSymbol(name, isRecursive);
        if (var == null) {
            throw new NullSymbolException(name);
        }
        else if (!(var instanceof SymVar)) {
            throw new NotVarException(name);
        } else {
            return (SymVar)var;
        }
    }

    public SymType getType(String name) throws Exception {
        Symbol type = this.getSymbol(name, true);
        if (type == null) {
            throw new NullSymbolException(name);
        }
        else if (!(type instanceof SymType)) {
            throw new NotTypeException(name);
        } else {
            return (SymType)type;
        }
    }

    public SymProc getProc(String name) throws Exception {
        Symbol proc = this.getSymbol(name, true);
        if (proc == null) {
            throw new NullSymbolException(name);
        }
        else if (!(proc instanceof SymProc)) {
            throw new NotProcedureException(name);
        } else {
            return (SymProc)proc;
        }
    }

    public SymProc getFunc(String name) throws Exception {
        Symbol proc = this.getSymbol(name, true);
        if (proc == null) {
            throw new NullSymbolException(name);
        }
        else if (!(proc instanceof SymFunc)) {
            throw new NotProcedureException(name);
        } else {
            return (SymProc)proc;
        }
    }

    public SymProc getCallable(String name) throws Exception {
        Symbol callable = this.getSymbol(name, true);
        if (callable == null) {
            throw new NullSymbolException(name);
        }
        else if (!(callable instanceof SymFunc) && !(callable instanceof SymProc)) {
            throw new NotCallableException(name);
        }
        else if (callable instanceof SymProc) {
            return (SymProc)callable;
        }
        else if (callable instanceof SymFunc) {
            return (SymFunc)callable;
        }
        return null;
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
