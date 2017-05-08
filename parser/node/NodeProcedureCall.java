package parser.node;

import parser.Utils;
import parser.symbol.Symbol;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NodeProcedureCall extends Node {
    public ArrayList<Node> params;
    public Symbol callable;

    public NodeProcedureCall() {
        this.params = null;
        this.callable = null;
    }

    public NodeProcedureCall(ArrayList<Node> params, Symbol callable) {
        this.params = params;
        this.callable = callable;
    }

    public String toString() {
        return callable.name;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println(String.format("BEGIN PROCEDURE %s CALL", this.callable.name));
        for (int i = 0; i < params.size(); ++i) {
            Utils.printIndent(depth+1);
            System.out.println("Arg number " + i);
            this.params.get(i).print(depth+2);
        }
        Utils.printIndent(depth);
        System.out.println("END PROCEDURE CALL");
    }
}
