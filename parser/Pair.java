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
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<?, ?> p = (Pair<?, ?>) o;
        return p.first == this.first && p.second == this.second;
    }
}