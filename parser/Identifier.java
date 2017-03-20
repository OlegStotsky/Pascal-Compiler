package parser;

public class Identifier extends Node {
	private String name;
	
	public Identifier() {
		this.name = null;
	}
	
	public Identifier(String name) {
		this.name = name;
	}
}
