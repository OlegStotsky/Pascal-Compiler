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
	public SymType type;
	
	public NodeBinOperation() {
		this.operation = null;
		this.left = null;
		this.right = null;
		this.type = null;
	}

	public NodeBinOperation(TokenTypes.TokenType operation, Node left, Node right) {
		this.operation = operation;
		this.left = left;
		this.right = right;
		this.type = null;
	}

	public void print(int depth) throws Exception {
		left.print(depth+1);
		Utils.printIndent(depth);
		System.out.println(TokenTypes.getInstance().getText(this.operation));
		right.print(depth+1);
	}

	public Symbol getType(SymTable symTable) throws Exception {
		if (this.type != null) {
			return this.type;
		}

		TypeManager.getInstance();
		SymType opType = TypeManager.getInstance().resolveBinOperationResultType((SymType)left.getType(symTable), (SymType)right.getType(symTable), operation);
		if (opType == null) {
			throw new IllegalOperandTypesException(TokenTypes.getInstance().getText(operation));
		}
		this.type = opType;
		return opType;
	}
}
