package parser.node;

import parser.Utils;
import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.SymVar;
import parser.symbol.Symbol;

public class NodeIdentifier extends Node {
	public String name;
	public SymType type;

	public NodeIdentifier() {
		this.name = null;
		this.type = null;
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

	public Symbol getType(SymTable symTable) throws Exception {
		if (this.type != null) {
			return this.type;
		}

		SymVar var = symTable.getVar(this.name, true);
		this.type = (SymType)var.getType();
		return this.type;
	}
}
