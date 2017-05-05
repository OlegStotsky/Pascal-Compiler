package parser.exceptions;

import parser.symbol.SymVar;
import parser.symbol.Symbol;

/**
 * Created by olegstotsky on 01.05.17.
 */
public class IllegalTypeCastException extends Exception {
    public Symbol from;
    public Symbol to;
    public int row;
    public int column;

    public IllegalTypeCastException(Symbol from, Symbol to, int row, int column) {
        super(String.format("Error at line %d, column %d : " +
                "can't cast type %s to type %s",
                row, column,
                from.name, to.name));
        this.from = from;
        this.to = to;
        this.row = row;
        this.column = column;
    }
}
