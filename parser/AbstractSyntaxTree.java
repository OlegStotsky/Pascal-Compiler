package parser;

import parser.node.Node;

public class AbstractSyntaxTree {
	public Node root;
	
	public AbstractSyntaxTree() {
		root = null;
	}

	public AbstractSyntaxTree(Node root) {
		this.root = root;
	}

	public void print() {
		root.print(0);
	}
}
