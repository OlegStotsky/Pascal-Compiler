package parser.exceptions;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class NotTypeException extends Exception {
    public String name;

    public NotTypeException(String name) {
        super(String.format("Error: %s isn't type", name));
        this.name = name;
    }

    public NotTypeException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't type", row, column, name));
    }
}