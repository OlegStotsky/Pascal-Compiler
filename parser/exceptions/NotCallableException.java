package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NotCallableException extends HasSuffixException {
    public String name;
    public String msgSuffix;

    public NotCallableException(String name) {
        super(String.format("Error: %s isn't callable", name));
        this.name = name;
        this.msgSuffix = String.format("%s isn't callable", name);
    }

    public NotCallableException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't callable", row, column, name));
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
