package parser.Node;

public class NodeStringConstant extends Node {
	public String value;

	public NodeStringConstant() {
		this.value = null;
	}

	public NodeStringConstant(String value) {
		this.value = value;
	}

	public void print(int depth) {
		printIndent(depth);
		System.out.println("\"" + value + "\"");
	}
}
