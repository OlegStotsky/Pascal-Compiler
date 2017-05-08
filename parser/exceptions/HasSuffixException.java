package parser.exceptions;

/**
 * Created by olegstotsky on 08.05.17.
 */
public abstract class HasSuffixException extends Exception {
    public HasSuffixException() {
        super("");
    }

    public HasSuffixException(String msg) {
        super(msg);
    }

    abstract String getSuffix();
}
