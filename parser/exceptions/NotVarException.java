package parser.exceptions;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class NotVarException extends Exception {
    public String name;

    public NotVarException(String name) {
        super(String.format("Error: %s isn't variable", name));
        this.name = name;
    }

    public NotVarException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't variable", row, column, name));
    }
}
