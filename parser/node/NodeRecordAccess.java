package parser.node;

import parser.Utils;
import parser.exceptions.UnexpectedTypeException;
import parser.symbol.*;

public class NodeRecordAccess extends Node {
    public Node left, right;
    public SymType type;

    public NodeRecordAccess() {
        this.left = null;
        this.right = null;
        this.type = null;
    }

    public NodeRecordAccess(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public void print(int depth) throws Exception {
        left.print(depth+1);
        Utils.printIndent(depth);
        System.out.println(".");
        right.print(depth+1);
    }

    @Override
    public SymType getType(SymTable symTable) throws Exception {
        if (this.type != null) {
            return this.type;
        }

        SymVar var = getVar(symTable);
        this.type = (SymType)var.getType();

        return this.type;
    }

    public SymVar getVar(SymTable symTable) throws Exception {
        if (this.left instanceof NodeIdentifier) {
            SymVar leftVar = symTable.getVar(((NodeIdentifier) this.left).name, true);
            return ((SymTypeRecord)leftVar.getType()).symTable.getVar(this.right.toString(), false);
        }
        else if (this.left instanceof NodeRecordAccess) {
            SymVar leftVar = ((NodeRecordAccess) this.left).getVar(symTable);
            return ((SymTypeRecord)leftVar.getType()).symTable.getVar(this.right.toString(), false);
        }
        return null;
    }
}
