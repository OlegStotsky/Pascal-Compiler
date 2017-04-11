package parser.statement;

import parser.node.Node;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class AssignmentStatement extends Statement {
    public Node node;

    AssignmentStatement() {
        this.node = null;
    }

    AssignmentStatement(Node node) {
        this.node = node;
    }
}
