package parser;

import java.util.ArrayList;
import java.util.HashMap;

import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;

public class Parser {
	private enum Precedence {FIRST, SECOND, THIRD, LAST}
	
	private Tokenizer tokenizer;
	private AbstractSyntaxTree ast;
	private HashMap<TokenTypes.TokenType, Precedence> priorities;
	private SymTable symTable;
	
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		this.ast = new AbstractSyntaxTree();
		this.priorities = new HashMap<>();
		this.symTable = new SymTable();
		
		this.priorities.put(TokenTypes.TokenType.NOT, Precedence.FIRST);
		this.priorities.put(TokenTypes.TokenType.ADDR, Precedence.FIRST);
		
		this.priorities.put(TokenTypes.TokenType.MUL, Precedence.SECOND);
		this.priorities.put(TokenTypes.TokenType.DIV, Precedence.SECOND);
		this.priorities.put(TokenTypes.TokenType.AND, Precedence.SECOND);
		
		this.priorities.put(TokenTypes.TokenType.PLUS, Precedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.MINUS, Precedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.OR, Precedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.XOR, Precedence.THIRD);
		
		this.priorities.put(TokenTypes.TokenType.EQUAL, Precedence.LAST);
		this.priorities.put(TokenTypes.TokenType.NOT_E, Precedence.LAST);
		this.priorities.put(TokenTypes.TokenType.LESS, Precedence.LAST);
		this.priorities.put(TokenTypes.TokenType.GRT, Precedence.LAST);
		this.priorities.put(TokenTypes.TokenType.LESS_E, Precedence.LAST);
		this.priorities.put(TokenTypes.TokenType.GRT_E, Precedence.LAST);
	}
	
	public void parse() throws Exception {
		tokenizer.nextToken();
		parseVarBlock();
		ast.print();
	}

	/**
	 * Parses variable declaration part of form
	 * <variable declaration part> ::=
	 * <empty> | var <variable declaration>; {variable declaration;}
	 */
	private void parseVarBlock() throws Exception {
		Token token = tokenizer.curToken();
		tokenizer.nextToken();
		do {
			token = tokenizer.curToken();
			ArrayList<Token> identifiers = parseIdentifiersList();
			token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.COLON);
			tokenizer.nextToken();
			SymType type = parseType();
			token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			tokenizer.nextToken();
			for (Token identifier : identifiers) {
				symTable.addSymbol(new SymVar(identifier.text, type));
			}
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}
	
	/**
	 * Parses variable declaration of form 
	 * <variable declaration> ::= <identifier> {, <identifier>} : <type>
	 */
	private ArrayList<Token> parseIdentifiersList() throws Exception {
		ArrayList<Token> identifiers = new ArrayList<Token>();
		do {
			Token identifier = tokenizer.curToken();
			expect(identifier, TokenTypes.TokenType.ID);
			identifiers.add(identifier);
			Token token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.COMMA) {
				tokenizer.nextToken();
			}
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);

		return identifiers;
	}
	
	/**
	 * Parses type of form
	 * <type> ::= <simple type> | <array type>
	 */
	private SymType parseType() throws Exception {
		Token token = tokenizer.curToken();
		SymType type;
		if (token.type == TokenTypes.TokenType.ARRAY) {
			type = parseArrayType();
		} else {
			type = parseSimpleType();
		}

		return type;
	}
	
	/**
	 * Parses simple type of form
	 * <simple type> ::= integer | float | char
	 */
	private SymType parseSimpleType() throws Exception {
		Token token = tokenizer.curToken();
		tokenizer.nextToken();
		if (token.text.equals("integer")) {
			return new SymTypeInteger();
		}
		if (token.text.equals("float")) {
			return new SymTypeFloat();
		}

		throw new Exception(String.format("Syntar error at line %d, column %d: error in type definition",
				token.row,
				token.column)
				);
	}

	/**
	 * Parses array type of form
	 * <array type> ::= array[<index range>] of <simple type>
	 */
	private SymTypeArray parseArrayType() throws Exception {
		//Eat left square bracket
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.LSQB);
		
		//Get range
		Token startIndex = tokenizer.nextToken();
		expect(startIndex, TokenTypes.TokenType.INT_CONST);
		Token doubleDot = tokenizer.nextToken();
		expect(doubleDot, TokenTypes.TokenType.DBL_DOT);
		Token endIndex = tokenizer.nextToken();
		expect(endIndex, TokenTypes.TokenType.INT_CONST);
		
		//Eat of
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.OF);

		//Get type
		SymType type = parseType();

		tokenizer.nextToken();

		return new SymTypeArray(type, startIndex.intVal, endIndex.intVal);
	}

	/**
	 * Parses expression of form
	 * <expression> ::= <simple expression> {(<=|>=|==|<|>) simple expression}*
	 */
	private Node parseExpression() throws Exception {
		Node result = parseSimpleExpression();
		while (priorities.get(tokenizer.curToken().type) == Precedence.LAST) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseSimpleExpression();
			result = new BinOperation(operation.text, result, right);
		}

		return result;
	}

	/**
	 * Parses simple expression of form
	 * <simple expression> ::= <term> {(+|-) <term>}*
	 */
	private Node parseSimpleExpression() throws Exception {
		Node result = parseTerm();
		while (priorities.get(tokenizer.curToken().type) == Precedence.THIRD) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseTerm();
			result = new BinOperation(operation.text, result, right);
		}

		return result;
	}

	/**
	 * Parses term of form
	 * <term> ::= <factor> {(*|/) <factor>}*
	 */
	private Node parseTerm() throws Exception {
		Node result = parseFactor();
		while (priorities.get(tokenizer.curToken().type) == Precedence.SECOND) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseFactor();
			result = new BinOperation(operation.text, result, right);
		}
		
		return result;
	}

	/**
	 * Parses factor of form
	 * <factor> ::= (+|-) id | int_const | (expression)
	 */
	private Node parseFactor() throws Exception {
		UnaryOperation factor = new UnaryOperation();
		Token token = tokenizer.curToken();
		if (token.type == TokenTypes.TokenType.MINUS || token.type == TokenTypes.TokenType.ADDR) {
			factor.operation = token.text;
			token = tokenizer.nextToken();
		}
		if (token.type == TokenTypes.TokenType.ID) {
			factor.value = parseIdentifier(true);
			return factor;
		}
		if (token.type == TokenTypes.TokenType.INT_CONST) {
			factor.value = new IntConst(token.intVal);
			tokenizer.nextToken();
			return factor;
		}
		if (token.type == TokenTypes.TokenType.LEFT_PARENTH) {
			tokenizer.nextToken();
			factor.value = parseExpression();
			expect(tokenizer.curToken(), TokenTypes.TokenType.RIGHT_PARENTH);
			tokenizer.nextToken();
			return factor;
		}
		
		throw new Exception(String.format("Syntax error at (%d %d) : unexpected token type %s", 
				token.row,
				token.column,
				token.type.toString())
				);
	}
	
	private Node parseIdentifier(Boolean isRecursive) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ID);
		Node result = new Identifier(token.text);
		while (isRecursive) {
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.DOT) {
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.ID);
				Node right = parseIdentifier(false);
				result = new RecordAccess(result, right);
			}
			else if (token.type == TokenTypes.TokenType.LSQB) {
				tokenizer.nextToken();
				Node index = parseExpression();
				token = tokenizer.curToken();
				expect(token, TokenTypes.TokenType.RSQB);
				result = new ArrayAccess(result, index);
			} else {
				isRecursive = false;
			}
		}
		return result;
	}


	private void expect(Token token, TokenTypes.TokenType tokenType) throws Exception {
		if (token.type != tokenType) {
			throw new Exception(String.format("Syntax error at line %d, column %d : %s expected but %s found",
					token.row, 
					token.column, 
					tokenType.toString(), 
					token.type.toString())
					);
		}
	}
}
