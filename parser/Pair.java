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

    public int hashCode() {
        return first.hashCode()*10 ^ second.hashCode()*15;
    }

    public boolean equals(Object other) {
        return this.hashCode() == other.hashCode();
    }
}