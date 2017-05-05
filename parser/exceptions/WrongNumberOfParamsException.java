package parser.exceptions;

import parser.symbol.Symbol;

/**
 * Created by olegstotsky on 02.05.17.
 */
public class WrongNumberOfParamsException extends Exception {
    public String functionName;
    public int row;
    public int column;
    public int expected;
    public int got;

    public WrongNumberOfParamsException(String functionName, int expectedNumberOfParams, int gotNumberOfParams) {
        super(String.format("Error in function %s call : %d number of params expected, but got %d",
                functionName,
                expectedNumberOfParams,
                gotNumberOfParams));
        this.expected = expectedNumberOfParams;
        this.got = gotNumberOfParams;
    }

    public WrongNumberOfParamsException(int row, int column, String functionName, int expectedNumberOfParams, int gotNumberOfParams) {
        super(String.format("Error in function %s call at line %d, column %d : %d number of params expected, but got %d",
                functionName,
                row,
                column,
                expectedNumberOfParams,
                gotNumberOfParams));

        this.expected = expectedNumberOfParams;
        this.got = gotNumberOfParams;
    }
}
