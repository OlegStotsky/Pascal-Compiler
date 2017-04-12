package parser.statement;

import java.util.ArrayList;

/**
 * Created by olegstotsky on 11.04.17.
 */
public class StmtProgram extends Statement {
    public ArrayList<Statement> statements;

    public StmtProgram() {
        statements = new ArrayList<>();
    }

    public void addStatement(Statement stmt) {
        this.statements.add(stmt);
    }
}
