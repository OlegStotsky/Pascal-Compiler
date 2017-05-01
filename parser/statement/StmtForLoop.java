package parser.statement;

import parser.Utils;
import parser.node.Node;
import parser.symbol.Symbol;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class StmtForLoop extends Statement {
    public boolean isDownTo;
    public Node rangeStart;
    public Node rangeEnd;
    public Statement stmt;
    public Symbol loopCounter;

    public StmtForLoop() {
        this.isDownTo = false;
        this.rangeStart = null;
        this.rangeEnd = null;
        this.stmt = null;
        this.loopCounter = null;
    }

    public StmtForLoop(boolean isDownTo, Node rangeStart, Node rangeEnd, Statement stmt, Symbol loopCounter) {
        this.isDownTo = isDownTo;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.stmt = stmt;
        this.loopCounter = loopCounter;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN FOR LOOP");
        Utils.printIndent(depth+1);
        System.out.println(String.format("FOR %s :=", this.loopCounter.name));
        this.rangeStart.print(depth+2);
        Utils.printIndent(depth+1);
        if (isDownTo) {
            System.out.println("DOWNTO");
        } else {
            System.out.println("TO");
        }
        this.rangeEnd.print(depth+2);
        Utils.printIndent(depth+1);
        System.out.println("DO");
        this.stmt.print(depth+2);
        Utils.printIndent(depth);
        System.out.println("END FOR LOOP");
    }
}
