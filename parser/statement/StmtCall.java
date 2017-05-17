package parser.statement;

import parser.node.Node;

/**
 * Created by olegstotsky on 05.05.17.
 */
public class StatementCall extends Statement {
    public Node nodeCall;

    public StatementCall() {
        this.nodeCall = null;
    }

    public StatementCall(Node nodeCall) {
        this.nodeCall = nodeCall;
    }

    public void print(int depth) throws Exception {
        if (this.nodeCall != null) {
            this.nodeCall.print(depth);
        }
    }
}
