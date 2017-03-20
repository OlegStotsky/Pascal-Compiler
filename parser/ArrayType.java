package parser;

public class ArrayType extends Type {
	public IndexRange indexRange;
	public SimpleType type;
	
	public ArrayType() {
		this.indexRange = null;
		this.type = null;
	}

	public ArrayType(IndexRange indexRange, SimpleType type) {
		this.indexRange = indexRange;
		this.type = type;
	}
}
