package parser.exceptions;

import parser.symbol.SymType;
import parser.symbol.SymVar;
import parser.symbol.Symbol;
import tokenizer.Token;
import tokenizer.TokenTypes;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class UnexpectedTypeException extends HasSuffixException {
    public Token token;
    public Symbol[] expectedTypes;
    public int row;
    public int column;
    String msg;
    String msgSuffix;
    public final int UNPOSITIONAL_PREF_LENGTH = "Error : ".length();
    public final int PREF_LENGTH = "Error at line %d, column %d : ".length();

    public UnexpectedTypeException(SymType foundType, SymType ... expectedTypes) {
        StringBuilder msg = new StringBuilder(("Error : "));
        for (int i = 0; i < expectedTypes.length; ++i) {
            msg.append(expectedTypes[i].name);
            msg.append(" ");
            if (i != (expectedTypes.length - 1)) {
                msg.append("or ");
            }
        }
        msg.append(String.format("expected but %s found", foundType.name));
        this.msg = msg.toString();
        this.token = token;
        this.expectedTypes = expectedTypes;
        this.row = row;
        this.column = column;
    }

    public UnexpectedTypeException(int row, int column, Symbol foundType, Symbol... expectedTypes) {
        StringBuilder msg = new StringBuilder(String.format("Error at line %d, column %d : ", row, column));
        for (int i = 0; i < expectedTypes.length; ++i) {
            msg.append(expectedTypes[i].name);
            msg.append(" ");
            if (i != (expectedTypes.length - 1)) {
                msg.append("or ");
            }
        }
        msg.append(String.format("expected but %s found", foundType.name));
        this.token = token;
        this.expectedTypes = expectedTypes;
        this.row = row;
        this.column = column;
        this.msg = msg.toString();
        this.msgSuffix = this.msg.substring(PREF_LENGTH);
    }

    public String getMessage() {
        return this.msg;
    }

    public String getSuffix() {
        return this.msgSuffix;
    }
}
