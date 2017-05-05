package parser;

import java.util.ArrayList;
import java.util.HashMap;

import parser.exceptions.*;
import parser.node.*;
import parser.statement.*;
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
	}

	public void print() throws Exception {
		this.symTable.print(0);
		this.program.print(0);
	}

	private void parseProgram() throws Exception {
		parseDeclSection(this.symTable);
		this.program = parseStmtBlock(this.symTable);
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
			tokenizer.nextToken();
			Token id = tokenizer.curToken();
			expect(id, TokenTypes.TokenType.ID);
			SymVarConst var;
			token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.EQUAL);
			token = tokenizer.nextToken();
			Node node = parseExpression(table);
			Symbol type = null;
			try {
				type = node.getType(table);
			} catch (IllegalOperandTypesException e) {
				throw new IllegalOperandTypesException(tokenizer.curToken().row, tokenizer.curToken().column, e.operation);
			}
			var = new SymVarConst(id.text, type);
			token = tokenizer.curToken();
			expect(token, TokenTypes.TokenType.SEMICOLON);
			table.addSymbol(id.text, var);
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
		ArrayList<SymVar> params = new ArrayList<>();
		tokenizer.nextToken();
		parseParams(localTable, params);
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RIGHT_PARENTH);
		token = tokenizer.nextToken();
		if (!isProcedure) {
			expect(token, TokenTypes.TokenType.COLON);
			token = tokenizer.nextToken();
			retType = parseSimpleType(localTable);
			tokenizer.nextToken();
		}
		parseDeclSection(localTable);
		StmtBlock stmt = parseStmtBlock(localTable);
		token = tokenizer.curToken();

		if (isProcedure) {
			result = new SymProc(identifier.text, localTable, stmt, params);
			this.symTable.addSymbol(identifier.text, result);
		} else {
			result = new SymFunc(identifier.text, localTable, stmt, retType, params);
			this.symTable.addSymbol(identifier.text , result);
		}
	}

	private StmtBlock parseStmtBlock(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.BEGIN);
		tokenizer.nextToken();
		ArrayList<Statement> stmts = new ArrayList<>();
		while (true) {
			Statement stmt = parseStatement(symTable);
			stmts.add(stmt);
			token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.END) {
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
				break;
			}
			else if (token.type == TokenTypes.TokenType.EOF) {
				expect(token, TokenTypes.TokenType.END);
			}
		}

		tokenizer.nextToken();
		return new StmtBlock(stmts);
	}

	private Statement parseStatement(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		if (token.type == TokenTypes.TokenType.ID) {
			Statement stmt = null;
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.LEFT_PARENTH) {
				tokenizer.goBack();
				Node call = parseCall(symTable);
				stmt = new StatementCall(call);
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.SEMICOLON);
				tokenizer.nextToken();
			} else {
				tokenizer.goBack();
				NodeAssignment assignment = parseAssignment(symTable);
				stmt = new StmtAssign(assignment);
				tokenizer.nextToken();
			}

			return stmt;
		}
		else if (token.type == TokenTypes.TokenType.BEGIN) {
			return parseStmtBlock(symTable);
		}
		else if (token.type == TokenTypes.TokenType.IF) {
			return parseIfElse(symTable);
		}
		else if (token.type == TokenTypes.TokenType.FOR) {
			return parseForLoop(symTable);
		}

		return null;
	}

	private Statement parseIfElse(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.IF);
		tokenizer.nextToken();
		Node ifExpr = parseExpression(symTable);
		Symbol ifExprType = ifExpr.getType(symTable);
		if (ifExpr.getType(symTable) != SymTypeBoolean.getInstance()) {
			throw new UnexpectedTypeException(tokenizer.curToken().row,
					tokenizer.curToken().column,
					ifExprType,
					SymTypeBoolean.getInstance());
		}
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.THEN);
		tokenizer.nextToken();
		Statement ifStmt = parseStatement(symTable);
		token = tokenizer.curToken();
		Statement elseStmt = null;
		Statement result = null;
		if (token.type == TokenTypes.TokenType.ELSE) {
			tokenizer.nextToken();
			elseStmt = parseStatement(symTable);
			result = new StmtIfElse(ifExpr, ifStmt, elseStmt);
			return result;
		} else {
			result = new StmtIfElse(ifExpr, ifStmt);
			return result;
		}

	}

	private Statement parseForLoop(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.FOR);
		Token loopCounterName = tokenizer.nextToken();
		expect(loopCounterName, TokenTypes.TokenType.ID);
		SymType type = SymTypeInteger.getInstance();
		Symbol loopCounter = new SymVar(loopCounterName.text, type);
		symTable.addSymbol(loopCounterName.text, loopCounter);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.ASSIGN);
		tokenizer.nextToken();
		Node rangeStart = parseExpression(symTable);
		Symbol rangeStartType = rangeStart.getType(symTable);
		if (rangeStartType != SymTypeInteger.getInstance()) {
			throw new UnexpectedTypeException(tokenizer.curToken().row,
					tokenizer.curToken().column,
					rangeStartType,
					SymTypeInteger.getInstance());
		}
		token = tokenizer.curToken();
		boolean isDownTo = false;
		if (token.type == TokenTypes.TokenType.DOWNTO) {
			isDownTo = true;
		}
		if (token.type != TokenTypes.TokenType.TO && token.type != TokenTypes.TokenType.DOWNTO) {
			throw new UnexpectedTokenException(token, TokenTypes.TokenType.TO, TokenTypes.TokenType.DOWNTO);
		}
		token = tokenizer.nextToken();
		Node rangeEnd = parseExpression(symTable);
		Symbol rangeEndType = rangeEnd.getType(symTable);
		if (rangeEndType != SymTypeInteger.getInstance()) {
			throw new UnexpectedTypeException(tokenizer.curToken().row,
					tokenizer.curToken().column,
					rangeEndType,
					SymTypeInteger.getInstance());
		}
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.DO);
		tokenizer.nextToken();
		Statement stmt = parseStatement(symTable);
		Statement result = new StmtForLoop(isDownTo, rangeStart, rangeEnd, stmt, loopCounter);
		return result;
	}

	private NodeAssignment parseAssignment(SymTable symTable) throws Exception {
		Node left  = parseIdentifier(symTable, true);
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ASSIGN);
		tokenizer.nextToken();
		Node right = parseExpression(symTable);
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.SEMICOLON);

		SymType firstType = (SymType)left.getType(symTable);
		SymType secondType = (SymType)right.getType(symTable);
		if (!TypeManager.getInstance().isLegalTypeCast(secondType, firstType)) {
			throw new IllegalTypeCastException(firstType, secondType, token.row, token.column);
		}

		return new NodeAssignment(left, right);
	}

	private void parseParams(SymTable symTable, ArrayList<SymVar> params) throws Exception {
		while (true) {
			SymVar var;
			Token name = tokenizer.curToken();
			Token token = tokenizer.nextToken();
			expect(token, TokenTypes.TokenType.COLON);
			token = tokenizer.nextToken();
			Symbol type = parseType(symTable);
			var = new SymVar(name.text, type);
			params.add(var);
			symTable.addSymbol(name.text, var);
			token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.COMMA) {
				tokenizer.nextToken();
				continue;
			}
			else if (token.type == TokenTypes.TokenType.RIGHT_PARENTH) {
				break;
			}
			throw new UnexpectedTokenException(token, TokenTypes.TokenType.RIGHT_PARENTH, TokenTypes.TokenType.COMMA);
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
		}
		else {
			type = parseSimpleType(symTable);
		}

		return type;
	}

	private Symbol parsePointerType(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.POINTER);
		Token ref = tokenizer.nextToken();
		Symbol refType = null;
		try {
			refType = symTable.getType(ref.text);
		}
		catch (NotTypeException e) {
			throw new NotTypeException(token.row, token.column, e.name);
		}
		catch (NullSymbolException e) {
			throw new NullSymbolException(e.name, token.row, token.column);
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
		Symbol type = null;
		try {
			type = symTable.getType(token.text);
		}
		catch (NullSymbolException e) {
			throw new NullSymbolException(e.name, token.row, token.column);
		}
		catch (NotTypeException e) {
			throw new NotTypeException(token.row, token.column, e.name);
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
		Node startIndex = parseExpression(symTable);
		Token doubleDot = tokenizer.curToken();
		expect(doubleDot, TokenTypes.TokenType.DBL_DOT);
		tokenizer.nextToken();
		Node endIndex = parseExpression(symTable);

		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RSQB);
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.OF);
		tokenizer.nextToken();

		Symbol type = parseType(symTable);

		result = new SymTypeArray(type, startIndex, endIndex);
		return result;
	}

	/**
	 * Parses expression of form
	 * <expression> ::= <simple expression> {(<=|>=|==|<|>) simple expression}*
	 */
	private Node parseExpression(SymTable symTable) throws Exception {
		Node result = parseSimpleExpression(symTable);
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.LAST) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseSimpleExpression(symTable);
			result = new NodeBinOperation(operation.type, result, right);
		}

		return result;
	}

	/**
	 * Parses simple expression of form
	 * <simple expression> ::= <term> {(+|-) <term>}*
	 */
	private Node parseSimpleExpression(SymTable symTable) throws Exception {
		Node result = parseTerm(symTable);
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.THIRD) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseTerm(symTable);
			result = new NodeBinOperation(operation.type, result, right);
		}

		return result;
	}

	/**
	 * Parses term of form
	 * <term> ::= <factor> {(*|/) <factor>}*
	 */
	private Node parseTerm(SymTable symTable) throws Exception {
		Node result = parseFactor(symTable);
		while (priorities.get(tokenizer.curToken().type) == OperationPrecedence.SECOND) {
			Token operation = tokenizer.curToken();
			tokenizer.nextToken();
			Node right = parseFactor(symTable);
			result = new NodeBinOperation(operation.type, result, right);
		}
		
		return result;
	}

	/**
	 * Parses factor of form
	 * <factor> ::= (+|-) id | int_const | (expression)
	 */
	private Node parseFactor(SymTable symTable) throws Exception {
		NodeUnaryOperation factor = new NodeUnaryOperation();
		Token token = tokenizer.curToken();
		if (token.type == TokenTypes.TokenType.MINUS || token.type == TokenTypes.TokenType.ADDR) {
			factor.operation = token.text;
			token = tokenizer.nextToken();
		}
		if (token.type == TokenTypes.TokenType.ID) {
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.LEFT_PARENTH) {
				tokenizer.goBack();
				Node call =  parseCall(symTable);
				if (! (call instanceof NodeFunctionCall)) {
					throw new NotFunctionException(tokenizer.curToken().row, tokenizer.curToken().column, call.toString());
				}
				factor.value = call;
			} else {
				tokenizer.goBack();
				factor.value = parseIdentifier(symTable, true);
			}
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
			factor.value = parseExpression(symTable);
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


	private Node parseIdentifier(SymTable symTable, Boolean isRecursive) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ID);
		Node result = new NodeIdentifier(token.text);
		while (isRecursive) {
			token = tokenizer.nextToken();
			if (token.type == TokenTypes.TokenType.DOT) {
				token = tokenizer.nextToken();
				expect(token, TokenTypes.TokenType.ID);
				Node right = parseIdentifier(symTable, false);
				result = new NodeRecordAccess(result, right);
			}
			else if (token.type == TokenTypes.TokenType.LSQB) {
				tokenizer.nextToken();
				Node index = parseExpression(symTable);
				token = tokenizer.curToken();
				expect(token, TokenTypes.TokenType.RSQB);
				result = new NodeArrayAccess(result, index);
			} else {
				isRecursive = false;
			}
		}

		return result;
	}

	private Node parseCall(SymTable symTable) throws Exception {
		Token token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.ID);
		Symbol proc = null;
		try {
			proc = symTable.getCallable(token.text);
		} catch (NotCallableException e) {
			throw new NotCallableException(tokenizer.curToken().row, tokenizer.curToken().column, token.text);
		}
		token = tokenizer.nextToken();
		expect(token, TokenTypes.TokenType.LEFT_PARENTH);
		tokenizer.nextToken();
		ArrayList<Node> params = parseParams();
		token = tokenizer.curToken();
		expect(token, TokenTypes.TokenType.RIGHT_PARENTH);

		try {
			checkParams((SymProc) proc, params, symTable);
		}
		catch (WrongNumberOfParamsException e) {
			throw new WrongNumberOfParamsException(tokenizer.curToken().row, tokenizer.curToken().column, proc.name, e.expected, e.got);
		}
		catch (WrongParamTypeException e) {
			throw new WrongParamTypeException(tokenizer.curToken().row, tokenizer.curToken().column, e.functionName,
					e.paramName, e.expectedType, e.gotType);
		}

		Node result = null;
		if (proc instanceof SymFunc) {
			result = new NodeFunctionCall(params, proc, ((SymFunc) proc).returnType);
		} else {
			result = new NodeProcedureCall(params, proc);
		}

		return result;
	 }

	void checkParams(SymProc callable, ArrayList<Node> params, SymTable symTable) throws Exception {
		if (callable.params.size() != params.size()) {
			throw new WrongNumberOfParamsException(callable.name, callable.params.size(), params.size());
		}

		for (int i = 0; i < params.size(); ++i) {
			if (!TypeManager.getInstance().isLegalTypeCast((SymType)params.get(i).getType(symTable), (SymType)callable.params.get(i).getType())) {
				throw new WrongParamTypeException(callable.name, callable.params.get(i).name, callable.params.get(i).getType(),
						params.get(i).getType(symTable));
			}
		}

	}

	private ArrayList<Node> parseParams() throws Exception {
		Token token;
		Node expr;
		ArrayList<Node> params = new ArrayList<Node>();
		do {
			expr = parseExpression(symTable);
			params.add(expr);
			token = tokenizer.curToken();
			if (token.type == TokenTypes.TokenType.COMMA) {
				tokenizer.nextToken();
			}
		} while (tokenizer.curToken().type != TokenTypes.TokenType.RIGHT_PARENTH);

		return params;
	}

	private void expect(Token token, TokenTypes.TokenType tokenType) throws Exception {
		if (token.type != tokenType) {
			throw new UnexpectedTokenException(token, tokenType);
		}
	}
}
