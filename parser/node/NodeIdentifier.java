package parser.node;

import parser.Utils;

public class NodeIdentifier extends Node {
	public String name;
	
	public NodeIdentifier() {
		this.name = null;
	}

	public NodeIdentifier(String name) {
		this.name = name;
	}

	public void print(int depth) {
		Utils.printIndent(depth);
		System.out.println(this.name);
	}

	public String toString() {
		return this.name;
	}
}
