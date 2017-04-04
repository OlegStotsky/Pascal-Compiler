package parser;

public class IntConst extends Node {
	public int val;
	
	public IntConst() {
		this.val = 0;
	}

	public IntConst(int val) {
		this.val = val;
	}

	public void print(int depth) {
		printIndent(depth);
		System.out.println(val);
	}
}
