package parser.symbol;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypePointer extends SymType {
    public SymType refType;

    public SymTypePointer() {
        this.refType = null;
    }

    public SymTypePointer(SymType refType) {
        this.refType = refType;
    }
}
