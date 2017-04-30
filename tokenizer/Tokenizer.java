package tokenizer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Tokenizer {
	private final int NUM_STATES = State.values().length;
	private final int NUM_CHARS = 128;
	private final String ERR_PREF = "Error at line %d, column %d : ";
	
	private enum State {
		BEGIN, NUMBER, 
		NUMBER_DBL_SECOND, NUMBER_DBL_FIRST,
		NUMBER_DBL_EXP_FIRST, NUMBER_DBL_EXP_SECOND,
		ERR_DBL_NO_FRACT, ERR_EXP,
		ID, SPECIAL_FIRST, SPECIAL_SECOND,
		SPECIAL_THIRD, SPECIAL_DBL, 
		CHAR_CONST, CHAR_CONST_ERR, 
		KEYWORD, COMMENT_SIMPLE, 
		COMMENT_MULTI, 
		END, SLASH, COMMENT_SINGLE,
		SKIP, LEFT_BRACKET, ERR_COMMENT_MULTI_EOF,
		COMMENT_BRACKET_FIRST, COMMENT_BRACKET_SECOND,
		ERR_COMMENT_BRACKET_LINEEND, ERR_COMMENT_BRACKET_EOF,
		DOT, DOUBLE_DOT, NUMBER_DBL_EXP_SIGN, 
		NUMBER_EXP_SIGN, EOF
	}
	
	private State statesTable[][];
	private FileReader input;
	private int curColumn;
	private int curRow;
	Boolean eof;
	byte lastByte;
	Boolean twoDots = false;
	Token lastToken;
	
	public Tokenizer(String fileName) throws IOException {
		try {
			this.input = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("File doesn`t exist");
			System.exit(-1);
		}
		this.statesTable = new State[NUM_STATES][NUM_CHARS];
		this.curColumn = 0;
		this.curRow = 0;
		this.eof = false;
		this.lastByte = (byte)input.read();
		this.lastToken = null;
		
		for (int i = 0; i < NUM_STATES; ++i) {
			for (int j = 0; j < NUM_CHARS; ++j) {
				statesTable[i][j] = State.END;
			}
		}
		
		
		/* Numbers */
		for (int i = '0'; i <= '9'; ++i) {
			this.statesTable[State.BEGIN.ordinal()][i] = State.NUMBER;
			this.statesTable[State.NUMBER.ordinal()][i] = State.NUMBER;
			this.statesTable[State.NUMBER_DBL_FIRST.ordinal()][i] = State.NUMBER_DBL_SECOND;
			this.statesTable[State.NUMBER_DBL_SECOND.ordinal()][i] = State.NUMBER_DBL_SECOND;
			this.statesTable[State.NUMBER_DBL_EXP_FIRST.ordinal()][i] = State.NUMBER_DBL_EXP_SECOND;
			this.statesTable[State.NUMBER_DBL_EXP_SECOND.ordinal()][i] = State.NUMBER_DBL_EXP_SECOND;
			this.statesTable[State.NUMBER_DBL_EXP_SIGN.ordinal()][i] = State.NUMBER_DBL_EXP_SECOND;
		}
		for (int i = 0; i < '0'; ++i) {
			this.statesTable[State.NUMBER_DBL_FIRST.ordinal()][i] = State.ERR_DBL_NO_FRACT;
			this.statesTable[State.NUMBER_DBL_EXP_FIRST.ordinal()][i] = State.ERR_EXP;
			this.statesTable[State.NUMBER_DBL_EXP_SIGN.ordinal()][i] = State.ERR_EXP;
			this.statesTable[State.NUMBER_EXP_SIGN.ordinal()][i] = State.ERR_EXP;
		}
		for (int i = '9' + 1; i < 128; ++i) {
			this.statesTable[State.NUMBER_DBL_FIRST.ordinal()][i] = State.ERR_DBL_NO_FRACT;
			this.statesTable[State.NUMBER_DBL_EXP_FIRST.ordinal()][i] = State.ERR_EXP;
			this.statesTable[State.NUMBER_DBL_EXP_SIGN.ordinal()][i] = State.ERR_EXP;
		}
		this.statesTable[State.NUMBER_DBL_SECOND.ordinal()]['E'] = State.NUMBER_DBL_EXP_FIRST;
		this.statesTable[State.NUMBER_DBL_SECOND.ordinal()]['e'] = State.NUMBER_DBL_EXP_FIRST;
		this.statesTable[State.NUMBER.ordinal()]['E'] = State.NUMBER_DBL_EXP_FIRST;
		this.statesTable[State.NUMBER.ordinal()]['e'] = State.NUMBER_DBL_EXP_FIRST;
		this.statesTable[State.NUMBER.ordinal()]['.'] = State.NUMBER_DBL_FIRST;
		this.statesTable[State.NUMBER.NUMBER_DBL_EXP_FIRST.ordinal()]['+'] = State.NUMBER_DBL_EXP_SIGN;
		this.statesTable[State.NUMBER_DBL_EXP_FIRST.ordinal()]['-'] = State.NUMBER_DBL_EXP_SIGN;
		this.statesTable[State.NUMBER_DBL_EXP_FIRST.ordinal()]['+'] = State.NUMBER_DBL_EXP_SIGN;
		this.statesTable[State.NUMBER_DBL_FIRST.ordinal()]['.'] = State.END;
		
		/* Words */
		for (int i = 'a'; i <= 'z'; ++i) {
			this.statesTable[State.BEGIN.ordinal()][i] = State.ID;
			this.statesTable[State.ID.ordinal()][i] = State.ID;
		}
		for (int i = 'A'; i <= 'Z'; ++i) {
			this.statesTable[State.BEGIN.ordinal()][i] = State.ID;
			this.statesTable[State.ID.ordinal()][i] = State.ID;
		}
		for (int i = '0'; i <= '9'; ++i) {
			this.statesTable[State.ID.ordinal()][i] = State.ID;
		}
		this.statesTable[State.ID.ordinal()]['_'] = State.ID;
		
		/* Specials */
		this.statesTable[State.BEGIN.ordinal()]['+'] = State.SPECIAL_FIRST;
		this.statesTable[State.BEGIN.ordinal()]['-'] = State.SPECIAL_FIRST; 
		this.statesTable[State.BEGIN.ordinal()]['*'] = State.SPECIAL_FIRST; 
		this.statesTable[State.BEGIN.ordinal()]['>'] = State.SPECIAL_FIRST; 
		this.statesTable[State.BEGIN.ordinal()]['='] = State.SPECIAL_FIRST;
		this.statesTable[State.BEGIN.ordinal()]['<'] = State.SPECIAL_SECOND;
		this.statesTable[State.BEGIN.ordinal()][':'] = State.SPECIAL_FIRST;
		this.statesTable[State.BEGIN.ordinal()]['@'] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()]['.'] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()]['['] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()][']'] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()][';'] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()][','] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()]['('] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()][')'] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()]['='] = State.SPECIAL_THIRD;
		this.statesTable[State.BEGIN.ordinal()]['^'] = State.SPECIAL_THIRD;
		this.statesTable[State.SPECIAL_FIRST.ordinal()]['='] = State.SPECIAL_DBL;
		this.statesTable[State.SPECIAL_SECOND.ordinal()]['='] = State.SPECIAL_DBL;
		this.statesTable[State.SPECIAL_SECOND.ordinal()]['>'] = State.SPECIAL_DBL;
		
		/* Left slash and single line comment type 1*/
		for (int i = 0; i < 128; ++i) {
			this.statesTable[State.COMMENT_SINGLE.ordinal()][i] = State.COMMENT_SINGLE;
		}
		this.statesTable[State.BEGIN.ordinal()]['/'] = State.SLASH;
		this.statesTable[State.SLASH.ordinal()]['/'] = State.COMMENT_SINGLE;
		this.statesTable[State.COMMENT_SINGLE.ordinal()]['\n'] = State.END;
		this.statesTable[State.SLASH.ordinal()]['='] = State.SPECIAL_DBL;
		this.statesTable[State.COMMENT_SINGLE.ordinal()][0] = State.END;
		
		/* MultiLine comment*/
		this.statesTable[State.BEGIN.ordinal()]['{'] = State.COMMENT_MULTI;
		for (int i = 0; i < 128; ++i) {
			this.statesTable[State.COMMENT_MULTI.ordinal()][i] = State.COMMENT_MULTI;
		}
		this.statesTable[State.COMMENT_MULTI.ordinal()][0] = State.ERR_COMMENT_MULTI_EOF;
		this.statesTable[State.COMMENT_MULTI.ordinal()]['}'] = State.END;
		
		/*Single line comment type 2 */
		this.statesTable[State.BEGIN.ordinal()]['('] = State.LEFT_BRACKET;
		this.statesTable[State.LEFT_BRACKET.ordinal()]['*'] = State.COMMENT_BRACKET_FIRST;
		for (int i = 0; i < 128; ++i) {
			this.statesTable[State.COMMENT_BRACKET_FIRST.ordinal()][i] = State.COMMENT_BRACKET_FIRST;
			this.statesTable[State.COMMENT_BRACKET_SECOND.ordinal()][i] = State.COMMENT_BRACKET_FIRST;
		}
		this.statesTable[State.COMMENT_BRACKET_FIRST.ordinal()]['*'] = State.COMMENT_BRACKET_SECOND;
		this.statesTable[State.COMMENT_BRACKET_SECOND.ordinal()][')'] = State.END;
		this.statesTable[State.COMMENT_BRACKET_FIRST.ordinal()]['\n'] = State.ERR_COMMENT_BRACKET_LINEEND;
		this.statesTable[State.COMMENT_BRACKET_FIRST.ordinal()][0] = State.ERR_COMMENT_BRACKET_LINEEND;
		this.statesTable[State.COMMENT_BRACKET_SECOND.ordinal()][0] = State.ERR_COMMENT_BRACKET_EOF;
		this.statesTable[State.COMMENT_BRACKET_SECOND.ordinal()]['\n'] = State.ERR_COMMENT_BRACKET_LINEEND;
		
		/* Char and string constant */
		this.statesTable[State.BEGIN.ordinal()]['\''] = State.CHAR_CONST;
		for (int i = 0; i < 128; ++i) {
			this.statesTable[State.CHAR_CONST.ordinal()][i] = State.CHAR_CONST;
		}
		this.statesTable[State.CHAR_CONST.ordinal()]['\''] = State.END;
		this.statesTable[State.CHAR_CONST.ordinal()]['\''] = State.END;
		this.statesTable[State.CHAR_CONST.ordinal()]['\n'] = State.CHAR_CONST_ERR;
		this.statesTable[State.CHAR_CONST.ordinal()][0] = State.CHAR_CONST_ERR;
		
		/* Dot */
		this.statesTable[State.BEGIN.ordinal()]['.'] = State.DOT;
		this.statesTable[State.DOT.ordinal()]['.'] = State.DOUBLE_DOT;	
		
		/* BackSpace */
		for (int i = 0; i < NUM_CHARS; ++i) {
			this.statesTable[State.SKIP.ordinal()][i] = this.statesTable[State.BEGIN.ordinal()][i];
		}
		this.statesTable[State.SKIP.ordinal()][' '] = State.SKIP;
		this.statesTable[State.SKIP.ordinal()]['\n'] = State.SKIP;
		this.statesTable[State.SKIP.ordinal()]['\t'] = State.SKIP;
		this.statesTable[State.BEGIN.ordinal()][' '] = State.SKIP;
		this.statesTable[State.BEGIN.ordinal()]['\n'] = State.SKIP;
		this.statesTable[State.BEGIN.ordinal()]['\t'] = State.SKIP;
		
		/* EOF */
		this.statesTable[State.BEGIN.ordinal()][0] = State.EOF;
	}
	
	/**
	 * Returns new token from the stream specified by constructor argument
	 */
	public Token nextToken() throws Exception {
		StringBuffer raw = new StringBuffer();
		Token result = new Token("", curRow, curColumn, 0, 0, TokenTypes.TokenType.UNKNOWN);
		State curState = State.BEGIN;
		State newState = State.BEGIN;
		Boolean isFirst = true;
		
		while (true) {
			byte curByte = isFirst ? lastByte : (byte)this.input.read();
			if (curByte == '\r') {
				curByte = (byte)this.input.read();
			}
			if (curByte == 10) {
				curByte = '\n';
			}
			isFirst = false;
			this.lastByte = curByte;
 			if (curByte == -1) {
				curByte = 0;
			}
			curState = newState;
			newState = this.statesTable[curState.ordinal()][curByte];
			
			switch (newState) {
			case END:
				switch (curState) {
				case NUMBER:
					result.type = TokenTypes.TokenType.INT_CONST;
					result.intVal = (int) (Integer.parseInt(raw.toString()));
					result.text = raw.toString();
					this.lastToken = result;
					return result;
					
				case ID:
					if (TokenTypes.getInstance().isKeyWord(raw.toString().toLowerCase())) {
						result.type = TokenTypes.getInstance().getType(raw.toString().toLowerCase());
						result.text = raw.toString().toLowerCase();
						this.lastToken = result;
						return result;
					}
					result.type = TokenTypes.TokenType.ID;
					result.text = raw.toString().toLowerCase();
					this.lastToken = result;
					return result;
					
				case NUMBER_DBL_SECOND:
				case NUMBER_DBL_EXP_SECOND:
					result.text = raw.toString();
					result.type = TokenTypes.TokenType.FLOAT_CONST;
					result.realVal = Double.parseDouble(raw.toString());
					this.lastToken = result;
					return result;
					
				case DOT:	
				case DOUBLE_DOT:	
				case SLASH:
				case SPECIAL_FIRST:
				case SPECIAL_SECOND:
				case SPECIAL_THIRD:
				case LEFT_BRACKET:
				case SPECIAL_DBL:
					result.text = raw.toString();
					result.type = TokenTypes.getInstance().getType(result.text);
					this.lastToken = result;
					return result;
					
				case CHAR_CONST:
					curColumn++;
					result.text = raw.substring(1);
					result.type = result.text.length() == 1? TokenTypes.TokenType.CHAR_CONST : TokenTypes.TokenType.STRING_CONST;
					this.lastByte = (byte)input.read();
					this.lastToken = result;
					return result;
					
				case COMMENT_MULTI:
				case COMMENT_SINGLE:
				case COMMENT_BRACKET_FIRST:
				case COMMENT_BRACKET_SECOND:
					curColumn++;
					raw = new StringBuffer();
					result.column = curColumn;
					result.row = curRow;
					this.lastByte = (byte)input.read();
					newState = State.BEGIN;
					isFirst = true;
					break;
					
				case NUMBER_DBL_FIRST:
					result.text = raw.toString().substring(0, raw.length()-1);
					result.type = TokenTypes.TokenType.INT_CONST;
					result.intVal = Integer.valueOf(result.text);
					twoDots = true;
					this.lastByte = curByte;
					this.lastToken = result;
					return result;
				
				case SKIP:
					newState = State.BEGIN;
					break;
			  }
			  break;
			
			case NUMBER:
			case NUMBER_DBL_SECOND:
			case NUMBER_DBL_EXP_SIGN:
			case NUMBER_DBL_EXP_SECOND:
			case ID:
			case LEFT_BRACKET:
				curColumn++;
				raw.append((char)curByte);
				break;
			
			case COMMENT_MULTI:
			case COMMENT_SINGLE:
			case COMMENT_BRACKET_FIRST:
			case COMMENT_BRACKET_SECOND:
			case SKIP:
				if ((char)curByte == '\t') {
					curColumn += 3;
				} else if ((char)curByte == '\n'){
					curRow++;
					curColumn = 0;
				} else {
					curColumn++;
				}
				result.column = curColumn;
				result.row = curRow;
				break;
				
			case SLASH:
			case NUMBER_DBL_EXP_FIRST:
			case CHAR_CONST:
			case SPECIAL_FIRST:
			case SPECIAL_SECOND:
			case SPECIAL_THIRD:
			case SPECIAL_DBL:
			case NUMBER_DBL_FIRST:
				if ((char)curByte == '\t') {
					curColumn += 3;
				} else if ((char)curByte == '\n'){
					curRow++;
					curColumn = 0;
				} else {
					curColumn++;
				}
				raw.append((char)curByte);
				break;
				 	
			case DOT:
			case DOUBLE_DOT:
				curColumn++;
				if (twoDots) {
					twoDots = false;
					lastByte = (byte)input.read();
					this.lastToken = new Token("..", curRow, curColumn-2, 0, 0, TokenTypes.TokenType.DBL_DOT);
					return this.lastToken;
				}
				raw.append((char)curByte);
				break;
					
			case ERR_DBL_NO_FRACT:
				throw new Exception(String.format(ERR_PREF + "bad real part", 
						this.curRow,
						this.curColumn));
				
			case ERR_EXP:
				throw new Exception(String.format(ERR_PREF + "bad exponent",
						this.curRow, 
						this.curColumn));
				
			case CHAR_CONST_ERR:
				if (raw.length() > 2) {
					throw new Exception(String.format(ERR_PREF + "bad string literal", 
							this.curRow, 
							this.curColumn));
				} else {
					throw new Exception(String.format(ERR_PREF + "bad char literal", 
							this.curRow, 
							this.curColumn));
				}
				
			case ERR_COMMENT_BRACKET_LINEEND:
				throw new Exception(String.format(ERR_PREF + "unexpected end of line while reading (* *) type comment", 
						this.curRow, 
						this.curColumn));
				
			case ERR_COMMENT_BRACKET_EOF:
				throw new Exception(String.format(ERR_PREF + "unexpected end of file while reading (* *) type comment", 
						this.curRow,
						this.curColumn));
				
			case ERR_COMMENT_MULTI_EOF:
				throw new Exception(String.format(ERR_PREF + "unexcepted end of file while reading {} type comment",
						this.curRow,
						this.curColumn));
				
			case EOF:
				this.eof = true;
				this.lastToken = new Token("EOF", this.curRow, this.curColumn, 0, 0, TokenTypes.TokenType.EOF);
				return this.lastToken;
		  }
	    }
	}
	
	public Token curToken() {
		return this.lastToken;
	}
	
	public Boolean eof() {
		return eof;
	}
}
