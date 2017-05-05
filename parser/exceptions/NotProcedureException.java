package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NotProcedureException extends Exception {
    public String name;

    public NotProcedureException(String name) {
        super(String.format("Error: %s isn't procedure", name));
        this.name = name;
    }

    public NotProcedureException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't procedure", row, column, name));
    }
}
