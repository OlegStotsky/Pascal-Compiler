package parser.node;

import parser.Utils;

public class NodeIntConst extends Node {
	public int val;
	
	public NodeIntConst() {
		this.val = 0;
	}

	public NodeIntConst(int val) {
		this.val = val;
	}

	public void print(int depth) {
		Utils.printIndent(depth);
		System.out.println(val);
	}
}
