package parser.node;

import parser.TypeManager;
import parser.Utils;
import parser.symbol.SymType;
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

	public SymType getType() throws Exception {
		SymType type = TypeManager.getInstance().resolveBinOperationResultType(left.getType(), right.getType(), operation);
		if (type == null) {
			throw new Exception("Can't cast types"); //TODO
		}
		return type;
	}
}
