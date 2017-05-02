package parser.node;

import parser.Utils;
import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.SymTypeInteger;
import parser.symbol.Symbol;

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

	public Symbol getType(SymTable symTable) throws Exception {
		return SymTypeInteger.getInstance();
	}
}
