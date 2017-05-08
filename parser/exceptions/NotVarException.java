package parser.exceptions;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class NotVarException extends HasSuffixException {
    public String name;
    public String msgSuffix;

    public NotVarException(String name) {
        super(String.format("Error: %s isn't variable", name));
        this.name = name;
        this.msgSuffix = String.format("%s isn't variable", name);
    }

    public NotVarException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't variable", row, column, name));
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
