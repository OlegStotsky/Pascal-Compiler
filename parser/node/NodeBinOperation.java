package parser.node;

import parser.TypeManager;
import parser.Utils;
import parser.exceptions.IllegalOperandTypesException;
import parser.symbol.SymTable;
import parser.symbol.SymType;
import parser.symbol.Symbol;
import tokenizer.Token;
import tokenizer.TokenTypes;

public class NodeBinOperation extends Node {
	public Node left;
	public Node right;
	public TokenTypes.TokenType operation;
	
	public NodeBinOperation() {
		this.operation = null;
		this.left = null;
		this.right = null;
	}

	public NodeBinOperation(TokenTypes.TokenType operation, Node left, Node right) {
		this.operation = operation;
		this.left = left;
		this.right = right;
	}

	public void print(int depth) throws Exception {
		left.print(depth+1);
		Utils.printIndent(depth);
		System.out.println(TokenTypes.getInstance().getText(this.operation));
		right.print(depth+1);
	}

	public Symbol getType(SymTable symTable) throws Exception {
		TypeManager.getInstance();
		SymType type = TypeManager.getInstance().resolveBinOperationResultType((SymType)left.getType(symTable), (SymType)right.getType(symTable), operation);
		if (type == null) {
			throw new IllegalOperandTypesException(TokenTypes.getInstance().getText(operation));
		}
		return type;
	}
}
