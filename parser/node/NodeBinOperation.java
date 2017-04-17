package parser.node;

import parser.TypeManager;
import parser.Utils;
import parser.symbol.SymType;

public class NodeBinOperation extends Node {
	public Node left;
	public Node right;
	public String operation;
	
	public NodeBinOperation() {
		this.operation = null;
		this.left = null;
		this.right = null;
	}

	public NodeBinOperation(String operation, Node left, Node right) {
		this.operation = operation;
		this.left = left;
		this.right = right;
	}

	public void print(int depth) {
		left.print(depth+1);
		Utils.printIndent(depth);
		System.out.println(operation);
		right.print(depth+1);
	}

	public SymType getType() throws Exception {
		SymType type = TypeManager.getInstance().resolveOperationResultType(left.getType(), right.getType());
		if (type == null) {
			throw new Exception("Can't cast types"); //TODO
		}
		return type;
	}
}
