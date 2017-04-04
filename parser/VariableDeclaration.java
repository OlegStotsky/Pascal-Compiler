package parser;

import java.util.ArrayList;

public class VariableDeclaration extends Node {
	public ArrayList<Identifier> identifiers;
	public Type type;
	
	public VariableDeclaration() {
		this.identifiers = null;
		this.type = null;
	}
	
	public VariableDeclaration(ArrayList<Identifier> identifiers, Type type) {
		this.identifiers = identifiers;
		this.type = type;
	}
}
