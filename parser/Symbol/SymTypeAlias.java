package parser.Symbol;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymTypeAlias extends SymType {
    public SymType refType;

    public SymTypeAlias() {
        this.refType = null;
    }

    public SymTypeAlias(SymType refType) {
        this.refType = refType;
    }
}
