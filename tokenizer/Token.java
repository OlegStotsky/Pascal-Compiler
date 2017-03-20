package tokenizer;

public class Token {
	public String text;
	public int row;
	public int column;
	public int intVal;
	public double realVal;
	public TokenTypes.TokenType type;
	
	public Token() {
		text = "";
		row = 0;
		column = 0;
		intVal = 0;
		realVal = 0;
		type = TokenTypes.TokenType.UNKNOWN;
	}
	
	public Token(String raw, int row, int column, 
			int intVal, double realVal, 
			TokenTypes.TokenType type) {
		
		this.text = raw;
		this.row = row;
		this.column = column;
		this.intVal = intVal;
		this.realVal = realVal;
		this.type = type;
	}

	public void print() {
		System.out.printf("%15s	%15s %15d %15d %15d %15f\n", 
				type.toString(), 
				text,
				row, 
				column, 
				intVal, realVal);
	}
}
