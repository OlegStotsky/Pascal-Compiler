package parser.statement;

import parser.Utils;
import parser.node.Node;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class StmtAssign extends Statement {
    public Node node;

    public StmtAssign() {
        this.node = null;
    }

    public StmtAssign(Node node) {
        this.node = node;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN STMT ASSIGN");
        node.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("END STMT ASSIGN");
    }
}
