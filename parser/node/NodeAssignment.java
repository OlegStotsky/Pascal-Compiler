package parser.node;

import tokenizer.TokenTypes;

/**
 * Created by olegstotsky on 10.04.17.
 */
public class NodeAssignment extends NodeBinOperation {
    public NodeAssignment() {
    }

    public NodeAssignment(Node left, Node right) {
        super(TokenTypes.TokenType.ASSIGN, left, right);
    }
}
