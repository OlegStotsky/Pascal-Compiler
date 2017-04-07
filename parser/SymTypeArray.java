package parser;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeArray extends SymType {
    public SymType elemType;
    public int start;
    public int end;

    public SymTypeArray() {
        this.elemType = null;
        this.start = 0;
        this.end = 0;
    }

    public SymTypeArray(SymType elemType, int start, int end) {
        this.elemType = elemType;
        this.start = start;
        this.end = end;
    }
}
