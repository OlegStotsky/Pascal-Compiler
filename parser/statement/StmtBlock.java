package parser.statement;

import parser.Utils;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 11.04.17.
 */
public class StmtBlock extends Statement {
    public ArrayList<Statement> statements;

    public StmtBlock() {
        this.statements = null;
    }

    public StmtBlock(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public void print(int depth) throws Exception {
        Utils.printIndent(depth);
        System.out.println("BEGIN STMT BLOCK");
        for (Statement statement : statements) {
            if (statement != null) {
                statement.print(depth + 1);
            }
        }
        Utils.printIndent(depth);
        System.out.println("END STMT BLOCK");
    }
}
