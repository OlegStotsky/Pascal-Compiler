package parser.node;

public abstract class Node {
    public void print(int depth) {
    }

    public void printIndent(int depth) {
        for (int i = 0; i < depth; ++i) {
            System.out.printf(" ");
        }
    }
}

