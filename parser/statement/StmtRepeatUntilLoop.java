package parser.statement;

import parser.Utils;
import parser.node.Node;

/**
 * Created by Stockiy.OV on 24.07.2017.
 */
public class StmtRepeatUntilLoop extends Statement {
    public Node condition;
    public Statement stmt;

    public StmtRepeatUntilLoop(Node condition, Statement stmt) {
        this.condition = condition;
        this.stmt = stmt;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN REPEAT UNTIL LOOP");
        Utils.printIndent(depth);
        System.out.println("REPEAT");
        stmt.print(depth+1);
        Utils.printIndent(depth);
        System.out.println("UNTIL");
        condition.print(depth+1);
    }
}
