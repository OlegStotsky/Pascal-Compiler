package parser.statement;

import parser.Utils;

/**
 * Created by olegstotsky on 16.05.17.
 */
public class StmtContinue extends Statement {
    public StmtContinue() {
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("CONTINUE");
    }
}
