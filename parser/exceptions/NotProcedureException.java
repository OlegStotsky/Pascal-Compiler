package parser.exceptions;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class NotProcedureException extends HasSuffixException {
    public String name;
    public String msgSuffix;

    public NotProcedureException(String name) {
        super(String.format("Error: %s isn't procedure", name));
        this.name = name;
        this.msgSuffix = String.format("%s isn't procedure", name);
    }

    public NotProcedureException(int row, int column, String name) {
        super(String.format("Error at line %d, column %d : %s isn't procedure", row, column, name));
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
