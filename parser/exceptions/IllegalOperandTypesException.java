package parser.exceptions;

import parser.symbol.Symbol;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class IllegalOperandTypesException extends HasSuffixException {
    public String operation;
    public int row;
    public int column;
    public String msg;
    public String msgSuffix;

    public IllegalOperandTypesException(String operation) {
        this.operation = operation;
        String msg = String.format("Error : Illegal operand types of operation %s", operation);
        this.msg = msg;
        this.msgSuffix = String.format("Illegal operand types of operation %s", operation);
    }

    public IllegalOperandTypesException(int row, int column, String operation) {
        this.row = row;
        this.column = column;
        this.operation = operation;
        String msg = String.format("Error at line %d, column %d : Illegal operand types of operation %s",
                row,
                column,
                operation);

        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
