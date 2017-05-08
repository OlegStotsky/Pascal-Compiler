package parser.statement;

import parser.Utils;
import parser.node.Node;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class StmtAssign extends Statement {
    public Node nodeAssignment;

    public StmtAssign() {
        this.nodeAssignment = null;
    }

    public StmtAssign(Node node) {
        this.nodeAssignment = node;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN STMT ASSIGN");
        nodeAssignment.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("END STMT ASSIGN");
    }
}
