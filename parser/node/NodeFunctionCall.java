package parser.node;

import parser.Utils;
import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.Symbol;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NodeFunctionCall extends NodeProcedureCall {
    public Symbol retType;

    public NodeFunctionCall() {
        this.retType = null;
    }

    public NodeFunctionCall(ArrayList<Node> params, Symbol callable, Symbol retType) {
        super(params, callable);
        this.retType = retType;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println(String.format("BEGIN FUNCTION %s CALL", this.callable.name));
        for (int i = 0; i < params.size(); ++i) {
            Utils.printIndent(depth+1);
            System.out.println("Arg number " + i);
            this.params.get(i).print(depth+2);
        }
        Utils.printIndent(depth);
        System.out.println("END FUNCTION CALL");
    }

    public Symbol getType(SymTable symTable) {
        return retType;
    }
}
