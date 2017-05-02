package parser.exceptions;

import tokenizer.Token;
import tokenizer.TokenTypes;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class UnexpectedTokenException extends Exception {
    Token token;
    TokenTypes.TokenType[] types;
    int row;
    int column;
    String msg;

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
        this.msg = msg.toString();
        this.token = token;
        this.types = types;
        this.row = row;
        this.column = column;
    }

    public String getMessage() {
        return this.msg;
    }
}
