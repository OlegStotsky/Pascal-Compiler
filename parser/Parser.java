package parser;

import java.util.ArrayList;
import java.util.HashMap;

import parser.node.*;
import parser.statement.Statement;
import parser.statement.StmtAssign;
import parser.statement.StmtBlock;
import parser.statement.StmtIfElse;
import parser.symbol.*;
import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;

public class Parser {
	public enum OperationPrecedence {FIRST, SECOND, THIRD, LAST}
	
	private Tokenizer tokenizer;
	private HashMap<TokenTypes.TokenType, OperationPrecedence> priorities;
	private Statement program;
	private SymTable symTable;
	private Node root;
	
	public Parser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
		this.priorities = new HashMap<>();
		this.symTable = new SymTable(null);
		this.symTable.symbols.put("integer", SymTypeInteger.getInstance());
		this.symTable.symbols.put("float", SymTypeFloat.getInstance());
		this.symTable.symbols.put("boolean", SymTypeBoolean.getInstance());
		this.symTable.symbols.put("char", SymTypeChar.getInstance());
		
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
		expect(tokenizer.curToken(), TokenTypes.TokenType.EOF);
		this.program.print(0);
	}

	private void parseProgram() throws Exception {
		while (!tokenizer.eof()) {
			Token token = tokenizer.curToken();
			parseDeclSection(this.symTable);
			if (token.type == TokenTypes.TokenType.BEGIN) {
				//tokenizer.nextToken();
				this.program = parseStmtBlock();
			}
			else {
				throw new Exception("Unexpected symbol");
			}
		}
	}

	private void parseDeclSection(SymTable symTable) throws Exception {
		while (!tokenizer.eof()) {
			Token token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.TYPE) {
				parseTypeBlock(symTable);
			}
			else if (token.type == TokenTypes.TokenType.VAR) {
				tokenizer.nextToken();
				parseVarBlock(this.symTable);
			}
			else if (token.type == TokenTypes.TokenType.CONST) {
				parseConstBlock(this.symTable);
			}
			else if (token.type == TokenTypes.TokenType.PROCEDURE) {
				parseProcedureOrFunctionBlock(true, symTable);
			}
			else if (token.type == TokenTypes.TokenType.FUNCTION) {
				parseProcedureOrFunctionBlock(false, symTable);
			}
			else {
				break;
			}
		}
	}

	private void parseTypeBlock(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.TYPE);
		tokenizer.nextToken();
		do {
			Token identifier = tokenizer.curToken();
			expect(identifier, TokenTypes.TokenType.ID);
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.EQUAL);
			token = tokenizer.nextToken();
			Symbol type = null;
			if (token.type == TokenTypes.TokenType.RECORD) {
				type = parseRecordType(symTable);
				type.name = identifier.text;
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.POINTER) {
				type = parsePointerType(symTable);
				type.name = identifier.text;
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.ID) {
				Symbol refType = symTable.getSymbol(token.text);
				type = new SymTypeAlias(identifier.text, refType);
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
				tokenizer.nextToken();
			}
			else if (token.type == TokenTypes.TokenType.ARRAY) {
				type = parseArrayType(symTable);
				type.name = identifier.text;
				expect(tokenizer.curToken(), TokenTypes.TokenType.SEMICOLON);
				tokenizer.nextToken();
			}

			symTable.addSymbol(identifier.text, type);

		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseConstBlock(SymTable table) throws Exception {
		do {
			Token token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.CONST);
			Token id = tokenizer.curToken();
			SymVar var;
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.EQUAL);
			token = tokenizer.nextToken();
			Node node = parseExpression();
			SymType type = node.getType(); //TODO: Probably need to check if type exists
			var = new SymVar(id.text, type);
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			tokenizer.nextToken();
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseVarBlock(SymTable symTable) throws Exception {
		do {
			ArrayList<Token> identifiers = parseIdentifiersList();
			Token token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.COLON);
			tokenizer.nextToken();
			Symbol type = parseType(symTable);
			token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			tokenizer.nextToken();
			for (Token identifier : identifiers) {
				Symbol var = new SymVar(identifier.text, type);
				symTable.addSymbol(identifier.text, var);
			}
		} while (tokenizer.curToken().type == TokenTypes.TokenType.ID);
	}

	private void parseProcedureOrFunctionBlock(Boolean isProcedure, SymTable parent) throws Exception {
		Token token = tokenizer.curToken();
		if (isProcedure) {
			expect(token, TokenTypes.TokenType.PROCEDURE);
		} else {
			expect(token, TokenTypes.TokenType.FUNCTION);
		}
		Symbol retType = null;
		Symbol result;
		Token identifier = tokenizer.nextToken();
		expect(identifier, TokenTypes.TokenType.ID);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.LEFT_PARENTH);
		SymTable localTable = new SymTable(parent);
		tokenizer.nextToken();
		parseParams(localTable);
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RIGHT_PARENTH);
		token = tokenizer.nextToken();
		if (!isProcedure) {
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			token = tokenizer.nextToken();
			retType = parseSimpleType(localTable);
			tokenizer.nextToken();
		}
		parseDeclSection(localTable);
		StmtBlock stmt = parseStmtBlock();
		token = tokenizer.curToken();

		if (isProcedure) {
			result = new SymProc(identifier.text, localTable, stmt);
			this.symTable.addSymbol(identifier.text, result);
		} else {
			result = new SymFunc(identifier.text, localTable, stmt, retType);
			this.symTable.addSymbol(identifier.text , result);
		}
	}

	private StmtBlock parseStmtBlock() throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.BEGIN);
		tokenizer.nextToken();
		ArrayList<Statement> stmts = new ArrayList<>();
		while (true) {
			Statement stmt = parseStatement();
			stmts.add(stmt);
			token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.END) {
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
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
			//tokenizer.nextToken();
			return parseStmtBlock();
		}
		else if (token.type == TokenTypes.TokenType.IF) {
			return parseIfElse();
		}

		return null;
	}

	private Statement parseIfElse() throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.IF);
		tokenizer.nextToken();
		Node ifExpr = parseExpression();
		//TODO: add type check here
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.THEN);
		tokenizer.nextToken();
		Statement ifStmt = parseStatement();
		token = tokenizer.curToken();
		Statement elseStmt = null;
		Statement result = null;
		if (token.type == TokenTypes.TokenType.ELSE) {
			tokenizer.nextToken();
			elseStmt = parseStatement();
			result = new StmtIfElse(ifExpr, ifStmt, elseStmt);
			return result;
		} else {
			result = new StmtIfElse(ifExpr, ifStmt);
			return result;
		}

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

	private void parseParams(SymTable symTable) throws Exception {
		while (true) {
			SymVar var;
			Token name = tokenizer.curToken();
			Token token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.COLON);
			token = tokenizer.nextToken();
			Symbol type = parseType(symTable);
			var = new SymVar(name.text, type);
			symTable.addSymbol(name.text, var);
			token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.COMMA) {
				tokenizer.nextToken();
				continue;
			}
			else if (token.type == TokenTypes.TokenType.RIGHT_PARENTH) {
				break;
			}
			throw new Exception("Error in param section");
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

	private Symbol parseType(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		Symbol type;
		if (token.type == TokenTypes.TokenType.ARRAY) {
			type = parseArrayType(symTable);
		}
		else if (token.type == TokenTypes.TokenType.RECORD) {
			type = parseRecordType(symTable);
		}
		else if (token.type == TokenTypes.TokenType.POINTER) {
			type = parsePointerType(symTable);
		} else {
			type = parseSimpleType(symTable);
		}

		return type;
	}

	private Symbol parsePointerType(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.POINTER);
		Token ref = tokenizer.nextToken();
		Symbol refType = symTable.getSymbol(ref.text);
		if (!(refType instanceof SymType)) {
			throw new Exception("Unknown type");
		}
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.SEMICOLON);
		Symbol type = new SymTypePointer(refType);
		return type;
	}

	private Symbol parseRecordType(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RECORD);
		SymTable table = new SymTable(symTable);
		tokenizer.nextToken();
		parseVarBlock(table);
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.END);
		Symbol type = new SymTypeRecord(table);
		tokenizer.nextToken();
		return type;
	}

	private Symbol parseSimpleType(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		Symbol type = symTable.getSymbol(token.text);
		if (type == null || !(type instanceof SymType)) {
			throw new Exception(String.format("Syntax error at line %d, column %d: error in type definition",
					token.row,
					token.column)
			);
		}

		tokenizer.nextToken();
		return type;
	}

	private Symbol parseArrayType(SymTable symTable) throws Exception {
		SymTypeArray result;
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ARRAY);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.LSQB);

		tokenizer.nextToken();
		Node startIndex = parseExpression();
		Token doubleDot = tokenizer.curToken();
		expect(doubleDot, TokenTypes.TokenType.DBL_DOT);
		tokenizer.nextToken();
		Node endIndex = parseExpression();

		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RSQB);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.OF);
		tokenizer.nextToken();

		Symbol type = parseType(symTable);
		if (!(type instanceof SymType)) {
			throw new Exception("Not type");//TODO
		}

		result = new SymTypeArray(type, startIndex, endIndex);
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
		if (token.type == TokenTypes.TokenType.ID) {
			factor.value = parseIdentifier(true);
			return factor;
		}
		if (token.type == TokenTypes.TokenType.INT_CONST) {
			factor.value = new NodeIntConst(token.intVal);
			tokenizer.nextToken();
			return factor;
		}
		if (token.type == TokenTypes.TokenType.FLOAT_CONST) {
			factor.value = new NodeFloatConst(token.realVal);
			tokenizer.nextToken();
			return factor;

		}
		if (token.type == TokenTypes.TokenType.STRING_CONST) {
			factor.value = new NodeStringConst(token.text);
			tokenizer.nextToken();
			return factor;
		}
		if (token.type == TokenTypes.TokenType.CHAR_CONST) {
			factor.value = new NodeCharConst(token.text.charAt(0));
			tokenizer.nextToken();
			return factor;
		}
		if (token.type == TokenTypes.TokenType.TRUE || token.type == TokenTypes.TokenType.FALSE) {
			factor.value = new NodeBoolConst(new Boolean(token.text));
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
