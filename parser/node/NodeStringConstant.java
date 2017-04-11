package parser.node;

import parser.Utils;

public class NodeStringConstant extends Node {
	public String value;

	public NodeStringConstant() {
		this.value = null;
	}

	public NodeStringConstant(String value) {
		this.value = value;
	}

	public void print(int depth) {
		Utils.printIndent(depth);
		System.out.println("\"" + value + "\"");
	}
}
