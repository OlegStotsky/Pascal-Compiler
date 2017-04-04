package parser;

import java.util.HashMap;

import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;

public class Parser {
	private enum Precedence {FIRST, SECOND, THIRD, LAST}
	
	private Tokenizer tokenizer;
	private AbstractSyntaxTree ast;
	private HashMap<TokenTypes.TokenType, Precedence> priorities;
	
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		this.ast = new AbstractSyntaxTree();
		this.priorities = new HashMap<>();
		
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
		this.ast.root = parseExpression();
		ast.print();
	}
	
	/**
	 * Parses variable declaration part of form
	 * <variable declaration part> ::= 
	 * <empty> | var <variable declaration>; {variable declaration;}
	 */
	private VariableDeclarationPart parseVariableDeclarationPart() throws Exception {
		Token token = tokenizer.nextToken();
		VariableDeclarationPart varDeclPart = new VariableDeclarationPart();
		if (token.type == TokenTypes.TokenType.VAR) {
			while (true) {
				VariableDeclaration varDecl = parseVariableDeclaration();
				varDeclPart.varDecls.add(varDecl);
				token = tokenizer.nextToken();
				if (token.type == TokenTypes.TokenType.BEGIN || token.type == TokenTypes.TokenType.PROCEDURE ||
					token.type == TokenTypes.TokenType.FUNCTION) {
					break;
				}
			}
		}
		
		return varDeclPart;
	}
	
	/**
	 * Parses variable declaration of form 
	 * <variable declaration> ::= <identifier> {, <identifier>} : <type>
	 */
	private VariableDeclaration parseVariableDeclaration() throws Exception {
		VariableDeclaration varDecl = new VariableDeclaration();
		while (true) {
			Token identifier = tokenizer.nextToken();
			expect(identifier, TokenTypes.TokenType.ID);
			Token token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.COLON) {
				break;
			}
			if (token.type == TokenTypes.TokenType.COMMA) {
				varDecl.identifiers.add(new Identifier(identifier.text));
			}
		}
		
		Type type = parseType();
		varDecl.type = type;
		return varDecl;
	}
	
	/**
	 * Parses type of form
	 * <type> ::= <simple type> | <array type>
	 */
	private Type parseType() throws Exception {
		Token token = tokenizer.nextToken();
		Type type;
		if (token.type == TokenTypes.TokenType.ARRAY) {
			type = parseArrayType();
			return type;
		}
		type = parseSimpleType();
		return type;
	}
	
	/**
	 * Parses simple type of form
	 * <simple type> ::= integer | float | char
	 */
	private SimpleType parseSimpleType() throws Exception {
		Token token = tokenizer.nextToken();
		if (token.text.equals("integer") ||
			token.text.equals("float") ||
			token.text.equals("char")) {
			
			return new SimpleType(token.text);
		}
		
		throw new Exception(String.format("Syntar error at (%d,%d): error in type definition",
				token.row,
				token.column)
				);
	}

	/**
	 * Parses array type of form
	 * <array type> ::= array[<index range>] of <simple type>
	 * Note that when this method is called, 'array' token has already been eaten
	 */
	private ArrayType parseArrayType() throws Exception {
		//Eat left square bracket
		Token token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.LSQB);
		
		//Get <index range>. After this method is finished, tokenizer`s state is right square bracket
		IndexRange indexRange = parseIndexRange();
		
		//Eat of
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.OF);
		
		SimpleType type = parseSimpleType();
		
		return new ArrayType(indexRange, type);
	}

	/**
	 * Parses index range of form
	 * <index range> ::= <integer constant>..<integer constant>
	 */
	private IndexRange parseIndexRange() throws Exception {
		Token startIndex = tokenizer.nextToken();
		expect(startIndex, TokenTypes.TokenType.INT_CONST);
		Token doubleDot = tokenizer.nextToken();
		expect(doubleDot, TokenTypes.TokenType.DBL_DOT);
		Token endIndex = tokenizer.nextToken();
		expect(endIndex, TokenTypes.TokenType.INT_CONST);
		
		return new IndexRange(startIndex.intVal, endIndex.intVal);
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
