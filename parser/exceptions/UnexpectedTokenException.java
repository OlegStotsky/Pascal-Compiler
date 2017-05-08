package parser.exceptions;

import tokenizer.Token;
import tokenizer.TokenTypes;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class UnexpectedTokenException extends HasSuffixException {
    public Token token;
    public TokenTypes.TokenType[] types;
    public int row;
    public int column;
    public String msg;
    public String msgSuffix;
    public final int PREF_LENGTH = "Syntax error at line %d, column %d : ".length();

    public UnexpectedTokenException(Token token, TokenTypes.TokenType ... types) {
        StringBuilder msg = new StringBuilder(String.format("Syntax error at line %d, column %d : ", token.row, token.column));
        for (int i = 0; i < types.length; ++i) {
            msg.append(types[i]);
            msg.append(" ");
            if (i != (types.length - 1)) {
                msg.append("or ");
            }
        }
        msg.append(String.format("expected but %s found", token.type));
        this.token = token;
        this.types = types;
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
