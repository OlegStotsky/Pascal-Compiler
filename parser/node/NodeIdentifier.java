package parser.node;

public class NodeIdentifier extends Node {
	public String name;
	
	public NodeIdentifier() {
		this.name = null;
	}

	public NodeIdentifier(String name) {
		this.name = name;
	}

	public void print(int depth) {
		printIndent(depth);
		System.out.println(name);
	}
}
