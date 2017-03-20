package parser;

import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;
import tokenizer.TokenTypes.TokenType;

public class Parser {
	private Tokenizer tokenizer;
	private AbstractSyntaxTree ast;
	
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
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
		if (token.text == "integer" ||
			token.text == "float" ||
			token.text == "char") {
			
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

	private void expect(Token token, TokenTypes.TokenType tokenType) throws Exception {
		if (token.type != tokenType) {
			throw new Exception(String.format("Syntax error at (%d,%d) : %s expected but %s found", 
					token.row, 
					token.column, 
					tokenType.toString(), 
					token.type.toString())
					);
		}
	}
}
