package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NullSymbolException extends Exception {
    public String name;

    public NullSymbolException(String name) {
        super(String.format("Error : couldn't find symbol %s", name));
        this.name = name;
    }

    public NullSymbolException(String name, int row, int column) {
        super(String.format("Error at line %d, column %d : couldn't find symbol %s", row, column, name));
    }
}
