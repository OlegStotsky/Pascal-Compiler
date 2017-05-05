package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NotCallableException extends Exception {
    public String name;

    public NotCallableException(String name) {
        super(String.format("Error: %s isn't callable", name));
        this.name = name;
    }

    public NotCallableException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't callable", row, column, name));
    }
}
