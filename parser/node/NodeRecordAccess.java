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

        //Recursively traverse a chain of NodeRecordAcceses until we get the type
        //Of the rightmost variable
        SymVar var = getVar(symTable);
        this.type = (SymType)var.getType();

        return this.type;
    }

    public SymVar getVar(SymTable symTable) throws Exception {
        //Base case of recursion : left node is not a record access.
        //We find a record on the left side, and get a value of right node
        //By using this record's symbol table.
        if (this.left instanceof NodeIdentifier) {
            SymVar leftVar = symTable.getVar(((NodeIdentifier) this.left).name, true);
            return ((SymTypeRecord)leftVar.getType()).symTable.getVar(this.right.toString(), false);
        }
        else if (this.left instanceof NodeFunctionCall) {
            return ((SymTypeRecord)((NodeFunctionCall)this.left).getType(symTable)).symTable.getVar(this.right.toString(), false);
        }
        else if (this.left instanceof NodeArrayAccess) {
            return ((SymTypeRecord)this.left.getType(symTable)).symTable.getVar(this.right.toString(), false);
        }

        //If left node is a record access, get type recursively
        else if (this.left instanceof NodeRecordAccess) {
            SymVar leftVar = ((NodeRecordAccess) this.left).getVar(symTable);
            return ((SymTypeRecord)leftVar.getType()).symTable.getVar(this.right.toString(), false);
        }

        return null;
    }
}
