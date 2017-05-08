package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NotFunctionException extends HasSuffixException {
    public String name;
    public String msgSuffix;

    public NotFunctionException(String name) {
        super(String.format("Error: %s isn't function", name));
        this.name = name;
        this.msgSuffix = String.format("%s isn't function", name);
    }

    public NotFunctionException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't function", row, column, name));
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
