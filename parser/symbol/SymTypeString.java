package parser.symbol;

/**
 * Created by olegstotsky on 11.04.17.
 */
public class SymTypeString extends SymType {
    public String value;

    public SymTypeString(String name, String value) {
        super(name);
        this.value = value;
    }
}
