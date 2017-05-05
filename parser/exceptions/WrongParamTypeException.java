package parser.exceptions;

import parser.symbol.SymType;
import parser.symbol.Symbol;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class WrongParamTypeException extends Exception {
    public String functionName;
    public String paramName;
    public Symbol expectedType;
    public Symbol gotType;
    public int row;
    public int column;

    public WrongParamTypeException(String functionName, String paramName, Symbol expectedType, Symbol gotType) {
        super(String.format("Error in function %s call : param %s should be of type %s, but got type %s",
                functionName,
                paramName,
                expectedType.name,
                gotType.name));

        this.functionName = functionName;
        this.paramName = paramName;
        this.expectedType = expectedType;
        this.gotType = gotType;
    }

    public WrongParamTypeException(int row, int column, String functionName, String paramName, Symbol expectedType, Symbol gotType) {
        super(String.format("Error in function %s call at line %d, column %d : param %s should be of type %s, but got type %s",
                row,
                column,
                functionName,
                paramName,
                expectedType.name,
                gotType.name));

        this.functionName = functionName;
        this.paramName = paramName;
        this.expectedType = expectedType;
        this.gotType = gotType;
    }
}
