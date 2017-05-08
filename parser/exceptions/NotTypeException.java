package parser.exceptions;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class NotTypeException extends HasSuffixException {
    public String name;
    public String msgSuffix;

    public NotTypeException(String name) {
        super(String.format("Error: %s isn't type", name));
        this.name = name;
        this.msgSuffix = String.format("%s isn't type", name);
    }

    public NotTypeException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't type", row, column, name));
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}