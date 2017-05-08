package parser.exceptions;

/**
 * Created by olegstotsky on 08.05.17.
 */
public class PositionalException extends Exception {
    public int row;
    public int column;

    public PositionalException(int row, int column, String suffix) {
        super(String.format("Error at line %d, column %d : ", row, column) + suffix);
    }
}
