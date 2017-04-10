package parser;

public class BinOperation extends Node {
	public Node left;
	public Node right;
	public String operation;
	
	BinOperation() {
		this.operation = null;
		this.left = null;
		this.right = null;
	}

	BinOperation(String operation, Node left, Node right) {
		this.operation = operation;
		this.left = left;
		this.right = right;
	}

	public void print(int depth) {
		left.print(depth+1);
		printIndent(depth);
		System.out.println(operation);
		right.print(depth+1);
	}
}
