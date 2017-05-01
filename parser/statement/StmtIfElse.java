package parser.statement;

import parser.Utils;
import parser.node.Node;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class StmtIfElse extends Statement {
    public Node ifExpr;
    public Statement ifStmt;
    public Statement elseStmt;

    public StmtIfElse() {
        ifExpr = null;
        ifStmt = null;
        elseStmt = null;
    }

    public StmtIfElse(Node ifExpr, Statement ifStmt, Statement elseStmt) {
        this.ifExpr = ifExpr;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    public StmtIfElse(Node ifExpr, Statement ifStmt) {
        this.ifExpr = ifExpr;
        this.ifStmt = ifStmt;
        this.elseStmt = null;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN STMT IF-ELSE");
        Utils.printIndent(depth+1);
        System.out.println("IF");
        this.ifExpr.print(depth+2);
        Utils.printIndent(depth+1);
        System.out.println("THEN");
        this.ifStmt.print(depth+2);
        if (this.elseStmt != null) {
            Utils.printIndent(depth+1);
            System.out.println("ELSE");
            this.elseStmt.print(depth+2);
        }
        Utils.printIndent(depth);
        System.out.println("END STMT IF-ELSE");
    }
}
