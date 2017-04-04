package parser;

import java.util.ArrayList;

public class VariableDeclarationPart extends Node {
	public ArrayList<VariableDeclaration> varDecls;
	
	public VariableDeclarationPart() {
		this.varDecls = null;
	}

	public VariableDeclarationPart(ArrayList<VariableDeclaration> varDecls) {
		this.varDecls = varDecls;
	}
}
