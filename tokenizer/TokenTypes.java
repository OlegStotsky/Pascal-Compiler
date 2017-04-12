package tokenizer;

public class TokenTypes {
	public enum TokenType {
		INT_CONST, FLOAT_CONST, STRING_CONST,
		CHAR_CONST, PLUS, MINUS, 
		MUL, DIV, POINTER, 
		GRT, LESS, ADDR, 
		DOT, LEFT_PARENTH, RIGHT_PARENTH, 
		LSQB, RSQB, SEMICOLON, 
		COLON, COMMA, IS_EQUAL,
		PLUS_E, MINUS_E, MUL_E, 
		DIV_E, GRT_E, LESS_E, 
		NOT_E, ASSIGN, DBL_DOT, 
		ID, BEGIN, END, 
		FORWARD, DO, FUNCTION, 
		PROCEDURE, RECORD, IF, 
		THEN, ELSE, FOR, 
		TO, REPEAT, UNTIL, 
		BREAK, CONTINUE, CASE, 
		VAR, TYPE, ARRAY, 
		OF, CONST, TRUE, 
		FALSE, AND, NOT, 
		OR, XOR, UNKNOWN,
		EQUAL, EOF
	}
	
	class KeyWord {
		public String text;
		public TokenType type;
		
		public KeyWord(String text, TokenType type) {
			this.text = text;
			this.type = type;
		}
	}
	
	class SpecialSymbol {
		String text;
		TokenType type;
		
		public SpecialSymbol(String text, TokenType type) {
			this.text = text;
			this.type = type;
		}
	}
	
	class SpecialSymbolPair {
		String text;
		TokenType type;
		
		public SpecialSymbolPair(String text, TokenType type) {
			this.text = text;
			this.type = type;
		}
	}
	
	public KeyWord keyWords[] = {
			new KeyWord("begin", TokenType.BEGIN),
			new KeyWord("end", TokenType.END),
			new KeyWord("forward", TokenType.FORWARD),
			new KeyWord("do", TokenType.DO), 
			new KeyWord("Function", TokenType.FUNCTION),
			new KeyWord("Procedure", TokenType.PROCEDURE),
			new KeyWord("Record", TokenType.RECORD),
			new KeyWord("if", TokenType.IF),
			new KeyWord("then", TokenType.THEN),
			new KeyWord("else", TokenType.ELSE),
			new KeyWord("for", TokenType.FOR),
			new KeyWord("to", TokenType.TO),
			new KeyWord("repeat", TokenType.REPEAT),
			new KeyWord("until", TokenType.UNTIL),
			new KeyWord("break", TokenType.BREAK),
			new KeyWord("continue", TokenType.CONTINUE),
			new KeyWord("case", TokenType.CASE),
			new KeyWord("var", TokenType.VAR),
			new KeyWord("type", TokenType.TYPE),
			new KeyWord("array", TokenType.ARRAY),
			new KeyWord("of", TokenType.OF),
			new KeyWord("const", TokenType.CONST),
			new KeyWord("true", TokenType.TRUE),
			new KeyWord("false", TokenType.FALSE),
			new KeyWord("and", TokenType.AND),
			new KeyWord("not", TokenType.NOT),
			new KeyWord("or", TokenType.OR),
			new KeyWord("xor", TokenType.XOR)
	};
	
	public SpecialSymbol specialSymbols[] = {
			new SpecialSymbol("+", TokenType.PLUS),
			new SpecialSymbol("-", TokenType.MINUS),
			new SpecialSymbol("*", TokenType.MUL),
			new SpecialSymbol("/", TokenType.DIV),
			new SpecialSymbol("<", TokenType.LESS),
			new SpecialSymbol(">", TokenType.GRT),
			new SpecialSymbol("@", TokenType.ADDR),
			new SpecialSymbol(".", TokenType.DOT),
			new SpecialSymbol("(", TokenType.LEFT_PARENTH),
			new SpecialSymbol(")", TokenType.RIGHT_PARENTH),
			new SpecialSymbol("[", TokenType.LSQB),
			new SpecialSymbol("]", TokenType.RSQB),
			new SpecialSymbol(";", TokenType.SEMICOLON),
			new SpecialSymbol(":", TokenType.COLON),
			new SpecialSymbol(",", TokenType.COMMA),
			new SpecialSymbol("=", TokenType.EQUAL),
			new SpecialSymbol("^", TokenType.POINTER)
	};
	
	public SpecialSymbolPair specialSymbolPairs[] = {
			new SpecialSymbolPair("+=", TokenType.PLUS_E),
			new SpecialSymbolPair("-=", TokenType.MINUS_E),
			new SpecialSymbolPair(">=", TokenType.GRT_E),
			new SpecialSymbolPair("<=", TokenType.LESS_E),
			new SpecialSymbolPair("<>", TokenType.NOT_E),
			new SpecialSymbolPair("*=", TokenType.MUL_E),
			new SpecialSymbolPair("/=", TokenType.DIV_E),
			new SpecialSymbolPair(":=", TokenType.ASSIGN),
			new SpecialSymbolPair("..", TokenType.DBL_DOT),
			new SpecialSymbolPair("==", TokenType.IS_EQUAL)
	};
	
	private static TokenTypes instance = new TokenTypes();
	
	public static TokenTypes getInstance() {
		return instance;
	}
	
	public Boolean isKeyWord(String text) {
		for (KeyWord keyWord: keyWords) {
			if (keyWord.text.equals(text)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean isOperator(String text) {
		for (SpecialSymbol operator: specialSymbols) {
			if (operator.text.equals(text)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Boolean isSpecialOperator(String text) {
		for (SpecialSymbolPair operator: specialSymbolPairs) {
			if (operator.text.equals(text)) {
				return true;
			}
		}
		
		return false;
	}
	
	public TokenType getType(String text) {
		for (KeyWord keyWord: keyWords) {
			if (keyWord.text.equals(text)) {
				return keyWord.type;
			}
		}
		
		for (SpecialSymbol operator: specialSymbols) {
			if (operator.text.equals(text)) {
				return operator.type;
			}
		}
		
		for (SpecialSymbolPair specialOperator: specialSymbolPairs) {
			if (specialOperator.text.equals(text)) {
				return specialOperator.type;
			}
		}
		
		return TokenType.UNKNOWN;
	}

	public String getText(TokenType type) throws Exception {
		for (KeyWord keyWord: keyWords) {
			if (keyWord.type == type) {
				return keyWord.text;
			}
		}

		for (SpecialSymbol operator: specialSymbols) {
			if (operator.type == type) {
				return operator.text;
			}
		}

		for (SpecialSymbolPair specialOperator: specialSymbolPairs) {
			if (specialOperator.type == type) {
				return specialOperator.text;
			}
		}

		throw new Exception("Unknown token type");
	}
}