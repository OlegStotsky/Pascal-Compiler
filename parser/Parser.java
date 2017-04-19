package parser;

import java.util.ArrayList;
import java.util.HashMap;

import parser.node.*;
import parser.statement.Statement;
import parser.statement.StmtAssign;
import parser.statement.StmtBlock;
import parser.statement.StmtProgram;
import parser.symbol.*;
import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;

public class Parser {
	public enum OperationPrecedence {FIRST, SECOND, THIRD, LAST}
	
	private Tokenizer tokenizer;
	private HashMap<TokenTypes.TokenType, OperationPrecedence> priorities;
	private Statement program;
	
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		this.priorities = new HashMap<>();
		
		this.priorities.put(TokenTypes.TokenType.NOT, OperationPrecedence.FIRST);
		this.priorities.put(TokenTypes.TokenType.ADDR, OperationPrecedence.FIRST);
		
		this.priorities.put(TokenTypes.TokenType.MUL, OperationPrecedence.SECOND);
		this.priorities.put(TokenTypes.TokenType.DIV, OperationPrecedence.SECOND);
		this.priorities.put(TokenTypes.TokenType.AND, OperationPrecedence.SECOND);
		
		this.priorities.put(TokenTypes.TokenType.PLUS, OperationPrecedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.MINUS, OperationPrecedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.OR, OperationPrecedence.THIRD);
		this.priorities.put(TokenTypes.TokenType.XOR, OperationPrecedence.THIRD);
		
		this.priorities.put(TokenTypes.TokenType.EQUAL, OperationPrecedence.LAST);
		this.priorities.put(TokenTypes.TokenType.NOT_E, OperationPrecedence.LAST);
		this.priorities.put(TokenTypes.TokenType.LESS, OperationPrecedence.LAST);
		this.priorities.put(TokenTypes.TokenType.GRT, OperationPrecedence.LAST);
		this.priorities.put(TokenTypes.TokenType.LESS_E, OperationPrecedence.LAST);
		this.priorities.put(TokenTypes.TokenType.GRT_E, OperationPrecedence.LAST);
	}
	
	public void parse() throws Exception {
		tokenizer.nextToken();
		parseProgram();
		GlobalSymTable.getInstance().print(0);

		expect(tokenizer.curToken(), TokenTypes.TokenType.EOF);
	}

	private void parseProgram() throws Exception {
		while (!tokenizer.eof()) {
			Token token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.TYPE) {
				tokenizer.nextToken();
				parseTypeBlock();
			}
			else if (token.type == TokenTypes.TokenType.PROCEDURE || token.type == TokenTypes.TokenType.FUNCTION) {
				parseProcedureOrFunctionBlock();
			}

			else if (token.type == TokenTypes.TokenType.VAR) {
				tokenizer.nextToken();
				parseVarBlock(GlobalSymTable.getInstance());
			}
			else if (token.type == TokenTypes.TokenType.CONST) {
				tokenizer.nextToken();
				parseConstBlock(GlobalSymTable.getInstance());
			}
			else if (token.type == TokenTypes.TokenType.BEGIN) {
				tokenizer.nextToken();
				this.program = parseStmtBlock();
			}
			else if (token.type == TokenTypes.TokenType.EOF) {
				break;
			}
			else {
				throw new Exception("Unexpected symbol");
			}
		}
	}

	private void parseTypeBlock() throws Exception {
		do {
			ArrayList<Token> identifiers = parseIdentifiersList();
			Token token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.EQUAL);
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.RECORD) {
				SymTable table = new LocalSymTable(GlobalSymTable.getInstance());
				tokenizer.nextToken();
				parseVarBlock(table);
				token = tokenizer.curToken();
				expect(token, TokenTypes.TokenType.END);
				for (Token identifier : identifiers) {
					SymTypeRecord record = new SymTypeRecord(identifier.text, table);
					GlobalSymTable.getInstance().addType(identifier.text, record);
				}
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.POINTER) {
				Token ref = tokenizer.nextToken();
				tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
				SymType refType = GlobalSymTable.getInstance().getType(ref.text);
				if (refType == null) {
					throw new Exception(String.format("Syntax error at line %d, column %d : unexpected token type %s",
							token.row,
							token.column,
							token.type.toString())
					);
				}2
				for (Token identifier : identifiers) {
					SymTypePointer pointer = new SymTypePointer(refType);
					GlobalSymTable.getInstance().addType(identifier.text, pointer);
				}
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.ID) {
				SymType refType = GlobalSymTable.getInstance().getType(token.text);
				if (refType == null) {
					throw new Exception(String.format("Syntax error at line %d, column %d : unexpected token type %s",
							token.row,
							token.column,
							token.type.toString())
					);
				}
				SymTypeAlias alias = new SymTypeAlias(token.text, refType);
				GlobalSymTable.getInstance().addType(token.text, alias);
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.ARRAY) {
				tokenizer.nextToken();
				SymTypeArray arrayType = parseArrayType();
				arrayType.name = identifiers.get(0).text;
				expect(tokenizer.curToken(), TokenTypes.TokenType.SEMICOLON);
				for (Token identifier : identifiers) {
					GlobalSymTable.getInstance().addType(identifier.text, arrayType);
				}
				tokenizer.nextToken();
			}

		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseConstBlock(SymTable table) throws Exception {
		do {
			Token id = tokenizer.curToken();
			SymVar var;
			Token token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.EQUAL);
			token = tokenizer.nextToken();
			Node node = parseExpression();
			SymType type = node.getType();
			var = new SymVar(id.text, type);
			table.addVarSafe(id.text, var, type);
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			tokenizer.nextToken();
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseVarBlock(SymTable table) throws Exception {
		Token token;
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
				Symbol var = new SymVar(identifier.text, type);
				table.addVar(identifier.text, var);
			}
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseProcedureOrFunctionBlock() throws Exception {
		Token token = tokenizer.curToken();
		Boolean isProcedure = false;
		SymType retType = null;
		Symbol result;
		if (token.type == TokenTypes.TokenType.PROCEDURE) {
			isProcedure = true;
		}
		Token name  = tokenizer.nextToken();
		expect(name, TokenTypes.TokenType.ID);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.LEFT_PARENTH);
		SymTable localTable = new LocalSymTable(GlobalSymTable.getInstance());
		tokenizer.nextToken();
		parseParams(localTable);
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RIGHT_PARENTH);
		token = tokenizer.nextToken();
		if (!isProcedure) {
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			token = tokenizer.nextToken();
			retType = GlobalSymTable.getInstance().getType(token.text);
			if (retType == null) {
				throw new Exception("");
			}
			tokenizer.nextToken();
		}
		if (token.type == TokenTypes.TokenType.VAR) {
			parseVarBlock(localTable);
		}
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.BEGIN);
		tokenizer.nextToken();
		StmtBlock stmt = parseStmtBlock();
		token = tokenizer.curToken();

		if (isProcedure) {
			result = new SymProc(name.text, localTable, stmt);
			GlobalSymTable.getInstance().addVar(name.text, result);
		} else {
			result = new SymFunc(name.text, localTable, stmt, retType);
			GlobalSymTable.getInstance().addVar(name.text , result);
		}
	}

	private StmtBlock parseStmtBlock() throws Exception {
		ArrayList<Statement> stmts = new ArrayList<>();
		while (true) {
			Statement stmt = parseStatement();
			stmts.add(stmt);
			Token token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.END) {
				break;
			}
			else if (token.type == TokenTypes.TokenType.EOF) {
				throw new Exception("Unexpected EOF");
			}
		}

		tokenizer.nextToken();
		return new StmtBlock(stmts);
	}

	private Statement parseStatement() throws Exception {
		Token token = tokenizer.curToken();
		if (token.type == TokenTypes.TokenType.ID) {
			NodeAssignment assignment = parseAssignment();
			tokenizer.nextToken();
			return new StmtAssign(assignment);
		}
		else if (token.type == TokenTypes.TokenType.BEGIN) {
			tokenizer.nextToken();
			return parseStmtBlock();
		}

		return null;
	}

	private NodeAssignment parseAssignment() throws Exception {
		Node left  = parseIdentifier(true);
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ASSIGN);
		tokenizer.nextToken();
		Node right = parseExpression();
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.SEMICOLON);

		return new NodeAssignment(left, right);
	}

	private void parseParams(SymTable table) throws Exception {
		while (true) {
			SymVar var;
			Token name = tokenizer.curToken();
			Token token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.COLON);
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.ID);
			SymType type = GlobalSymTable.getInstance().getType(token.text);
			var = new SymVar(name.text, type);
			table.addVarSafe(name.text, var, type);
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.COMMA) {
				tokenizer.nextToken();
				continue;
			}
			else if (token.type == TokenTypes.TokenType.RIGHT_PARENTH) {
				break;
			}
			throw new Exception("");
		}
	}

	private ArrayList<Token> parseIdentifiersList() throws Exception {
		ArrayList<Token> identifiers = new ArrayList<>();
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

	private SymType parseType() throws Exception {
		Token token = tokenizer.curToken();
		SymType type;
		if (token.type == TokenTypes.TokenType.ARRAY) {
			tokenizer.nextToken();
			type = parseArrayType();
		} else {
			type = parseSimpleType();
		}

		return type;
	}

	private SymType parseSimpleType() throws Exception {
		Token token = tokenizer.curToken();
		SymType type = GlobalSymTable.getInstance().getType(token.text);
		tokenizer.nextToken();
		if (type == null) {
			throw new Exception(String.format("Syntar error at line %d, column %d: error in type definition",
					token.row,
					token.column)
			);
		}

		return type;
	}

	private SymTypeArray parseArrayType() throws Exception {
		SymTypeArray result;
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.LSQB);

		Token startIndex = tokenizer.nextToken();
		expect(startIndex, TokenTypes.TokenType.INT_CONST);
		Token doubleDot = tokenizer.nextToken();
		expect(doubleDot, TokenTypes.TokenType.DBL_DOT);
		Token endIndex = tokenizer.nextToken();
		expect(endIndex, TokenTypes.TokenType.INT_CONST);

		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.RSQB);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.OF);
		tokenizer.nextToken();

		SymType type = parseType();

		result = new SymTypeArray(type, startIndex.intVal, endIndex.intVal);
		return result;
	}

	/**
	 * Parses expression of form
	 * <expression> ::= <simple expression> {(<=|>=|==|<|>) simple expression}*
	 */
	private Node parseExpression() throws Exception {
		Node result = parseSimpleExpression();
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.LAST) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseSimpleExpression();
			result = new NodeBinOperation(operation.type, result, right);
		}

		return result;
	}

	/**
	 * Parses simple expression of form
	 * <simple expression> ::= <term> {(+|-) <term>}*
	 */
	private Node parseSimpleExpression() throws Exception {
		Node result = parseTerm();
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.THIRD) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseTerm();
			result = new NodeBinOperation(operation.type, result, right);
		}

		return result;
	}

	/**
	 * Parses term of form
	 * <term> ::= <factor> {(*|/) <factor>}*
	 */
	private Node parseTerm() throws Exception {
		Node result = parseFactor();
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.SECOND) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseFactor();
			result = new NodeBinOperation(operation.type, result, right);
		}
		
		return result;
	}

	/**
	 * Parses factor of form
	 * <factor> ::= (+|-) id | int_const | (expression)
	 */
	private Node parseFactor() throws Exception {
		NodeUnaryOperation factor = new NodeUnaryOperation();
		Token token = tokenizer.curToken();
		if (token.type == TokenTypes.TokenType.MINUS || token.type == TokenTypes.TokenType.ADDR) {
			factor.operation = token.text;
			token = tokenizer.nextToken();
		}
		else if (token.type == TokenTypes.TokenType.ID) {
			factor.value = parseIdentifier(true);
			return factor;
		}
		else if (token.type == TokenTypes.TokenType.INT_CONST) {
			factor.value = new NodeIntConst(token.intVal);
			tokenizer.nextToken();
			return factor;
		}
		else if (token.type == TokenTypes.TokenType.FLOAT_CONST) {
			factor.value = new NodeFloatConst(token.realVal);
			tokenizer.nextToken();
			return factor;

		}
		else if (token.type == TokenTypes.TokenType.STRING_CONST) {
			factor.value = new NodeStringConst(token.text);
			tokenizer.nextToken();
			return factor;
		}
		else if (token.type == TokenTypes.TokenType.CHAR_CONST) {
			factor.value = new NodeCharConst(token.text.charAt(0));
			tokenizer.nextToken();
			return factor;
		}
		else if (token.type == TokenTypes.TokenType.TRUE || token.type == TokenTypes.TokenType.FALSE) {
			factor.value = new NodeBoolConst(new Boolean(token.text));
			tokenizer.nextToken();
			return factor;
		}
		else if (token.type == TokenTypes.TokenType.LEFT_PARENTH) {
			tokenizer.nextToken();
			factor.value = parseExpression();
			expect(tokenizer.curToken(), TokenTypes.TokenType.RIGHT_PARENTH);
			tokenizer.nextToken();
			return factor;
		}

		throw new Exception(String.format("Syntax error at line %d, column %d : unexpected token type %s",
				token.row,
				token.column,
				token.type.toString())
				);
	}


	private Node parseIdentifier(Boolean isRecursive) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ID);
		Node result = new NodeIdentifier(token.text);
		while (isRecursive) {
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.DOT) {
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.ID);
				Node right = parseIdentifier(false);
				result = new NodeRecordAccess(result, right);
			}
			else if (token.type == TokenTypes.TokenType.LSQB) {
				tokenizer.nextToken();
				Node index = parseExpression();
				token = tokenizer.curToken();
				expect(token, TokenTypes.TokenType.RSQB);
				result = new NodeArrayAccess(result, index);
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
