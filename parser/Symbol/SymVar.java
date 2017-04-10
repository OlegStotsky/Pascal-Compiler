package parser.Symbol;

/**
 * Created by olegstotsky on 07.04.17.
 */
public class SymVar extends Symbol {
    public SymType type;

    public SymVar() {
        this.type = null;
    }

    public SymVar(String name, SymType type) {
        super(name);
        this.type = type;
    }
}
