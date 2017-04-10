package parser;

public class Identifier extends Node {
	public String name;
	
	public Identifier() {
		this.name = null;
	}

	public Identifier(String name) {
		this.name = name;
	}

	public void print(int depth) {
		printIndent(depth);
		System.out.println(name);
	}
}
