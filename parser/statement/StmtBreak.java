package parser.statement;

import parser.Utils;

/**
 * Created by olegstotsky on 16.05.17.
 */
public class StmtBreak extends Statement {
    public StmtBreak() {
    }

    public void print(int depth) {
        Utils.printIndent(depth);
        System.out.println("BREAK");
    }
}
