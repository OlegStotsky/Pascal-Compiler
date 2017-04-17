package parser.node;

import parser.Utils;

public class NodeStringConst extends Node {
	public String value;

	public NodeStringConst() {
		this.value = null;
	}

	public NodeStringConst(String value) {
		this.value = value;
	}

	public void print(int depth) {
		Utils.printIndent(depth);
		System.out.println("\"" + value + "\"");
	}
}
