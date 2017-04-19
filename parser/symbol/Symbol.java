package parser.symbol;

/**
 * Created by olegstotsky on 07.04.17.
 */
public abstract class Symbol {
    public String name;

    public Symbol() {
        this.name = null;
    }

    public Symbol(String name) {
        this.name = name;
    }

    public void print(int depth) throws Exception {

    }

    public String toString() {
        return this.name;
    }
}
