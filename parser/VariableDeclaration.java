package parser;

import java.util.ArrayList;

public class VariableDeclaration extends Node {
	ArrayList<Identifier> identifiers;
	Type type;
	
	public VariableDeclaration() {
		this.identifiers = null;
		this.type = null;
	}
	
	public VariableDeclaration(ArrayList<Identifier> identifiers, Type type) {
		this.identifiers = identifiers;
		this.type = type;
	}
}
