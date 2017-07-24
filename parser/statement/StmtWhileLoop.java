package parser.statement;

import parser.Utils;
import parser.node.Node;

/**
 * Created by Stockiy.OV on 24.07.2017.
 */
public class StmtWhileLoop extends Statement {
    public Node condition;
    public Statement stmt;

    public StmtWhileLoop(Node condition, Statement stmt) {
        this.condition = condition;
        this.stmt = stmt;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN WHILE LOOP");
        Utils.printIndent(depth+1);
        System.out.println("WHILE");
        condition.print(depth+2);
        Utils.printIndent(depth+1);
        System.out.println("DO");
        stmt.print(depth+2);
        Utils.printIndent(depth);
        System.out.println("END WHILE LOOP");
    }
}
