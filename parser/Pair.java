package parser;

/**
 * Created by olegstotsky on 16.04.17.
 */
public class Pair<U, V> {
    public U first;
    public V second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public Boolean equals(Pair<U, V> other) {
        return this.first.equals(other.first) & this.second.equals(other.second);
    }
}