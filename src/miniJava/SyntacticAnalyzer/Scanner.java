/**
 *  Scan the a single line of input
 *
 *  Grammar:
 *   num ::= digit digit*
 *   digit ::= '0' | ... | '9'
 *   oper ::= '+' | '*'
 *   
 *   whitespace is the space character
 */
package miniJava.SyntacticAnalyzer;

import java.io.*;

import miniJava.ErrorReporter;

public class Scanner{

	private InputStream inputStream;
	private ErrorReporter reporter;

	private char currentChar;
	private StringBuilder currentSpelling;
	
	private StringBuilder wholeString;

	
	// true when end of line is found
	private boolean eot = false; 
	
	private boolean inBlockComment = false;
	private boolean inLineComment = false;
	
	private int lineNumber;
	
	private int startingLine;

	public Scanner(InputStream inputStream, ErrorReporter reporter) {
		this.inputStream = inputStream;
		this.reporter = reporter;
		this.wholeString = new StringBuilder();
		this.lineNumber = 1;
		startingLine = 0;
		readChar();
	}

	/**
	 * skip whitespace and scan next token
	 */
	public Token scan() {
	
		while (!eot && (currentChar == ' ' || currentChar == eolWindows || currentChar == eolUnix || currentChar == '\t')) {
			wholeString.append(currentChar);
			skipIt();
		}
		
		currentSpelling = new StringBuilder();
		SourcePosition pos = new SourcePosition();
		//pos.start = lineNumber;
		TokenKind token = scanToken();
		String spelling = currentSpelling.toString();
		pos.start = startingLine;
		pos.finish = lineNumber;
		//wholeString.append(spelling);
		return new Token(token, spelling, pos);
	}

	/**
	 * determine token kind
	 */
	public TokenKind scanToken() {
		if (eot)
			return(TokenKind.EOT); 
		startingLine = lineNumber;
		// scan Token
		switch (currentChar) {
		
		// Tokens With Length Greater than 1
		case 'a':  case 'A': case 'b': case 'B': case 'c': case 'C':
		case 'd':  case 'D': case 'e': case 'E': case 'f': case 'F':
		case 'g':  case 'G': case 'h': case 'H': case 'i': case 'I':
		case 'j':  case 'J': case 'k': case 'K': case 'l': case 'L':
		case 'm':  case 'M': case 'n': case 'N': case 'o': case 'O':
		case 'p':  case 'P': case 'q': case 'Q': case 'r': case 'R':
		case 's':  case 'S': case 't': case 'T': case 'u': case 'U':
		case 'v':  case 'V': case 'w': case 'W': case 'x': case 'X':
		case 'y':  case 'Y': case 'z': case 'Z':
			takeIt();
			while(isLetterDigitUnderscore(currentChar)) {
				takeIt();
			}
			return(determineKind(currentSpelling.toString()));
			
			
		// Relational Operators (and negation)
		case '<':
			takeIt();
			if (currentChar == '=') {
				takeIt();
				return TokenKind.LTEQ;
			}
			return TokenKind.LT;
		case '>':
			takeIt();
			if (currentChar == '=') {
				takeIt();
				return TokenKind.GTEQ;
			}
			return TokenKind.GT;
		case '=':
			takeIt();
			if (currentChar == '=') {
				takeIt();
				return TokenKind.EQUALS;
			}
			return TokenKind.ASSIGNMENT;
		case '!':
			takeIt();
			if (currentChar == '=') {
				takeIt();
				return TokenKind.NEQ;
			}
			return TokenKind.NOT;
					
		// Logical Operators
		case '&':
			takeIt();
			if (currentChar == '&') {
				takeIt();
				return TokenKind.AND;
			}
			System.exit(4);
			
		case '|':
			takeIt();
			if (currentChar == '|') {
				takeIt();
				return TokenKind.OR;
			}
			System.exit(4);
			
		// Arithmetic Operators
		case '+':
			takeIt();
			return(TokenKind.PLUS);
		case '-':
			takeIt();
			return(TokenKind.MINUS);
		case '*':
			takeIt();
			if (currentChar == '/') {
				//End block comment
				takeIt();
				if (this.inBlockComment) {
					this.inBlockComment = false;
					return(TokenKind.ENDCOMMENT);
				} else {
					scanError("Ending Non-existant block comment");
					System.exit(4);
				}
			} else {
				return(TokenKind.TIMES);	
			}
		case '/':
			takeIt();
			if (currentChar == '/' && !this.inBlockComment) {
				//Line comment
				takeIt();
				this.inLineComment = true;
				while (currentChar != eolUnix && currentChar != eolWindows && !eot) {
					nextChar();
				}
				nextChar();
				if (eot) {
					return TokenKind.EOT;
				}
				
				this.inLineComment = false;
				currentSpelling = new StringBuilder();
				skipWithNewLines();
				return scanToken();
				
			} else if (currentChar == '*' && !this.inLineComment) {
				//takeIt();
				// Start of block comment
				nextChar();
				this.inBlockComment = true;
				//skipWithNewLines();
				//TokenKind temp = scanToken();
				while (true) {
					if (eot) {
						scanError("Unterminated Block Comment");
						System.exit(4);
					}
					if (currentChar == '*') {
						nextChar();
						if (currentChar == '/'){
							nextChar();
							currentSpelling = new StringBuilder();
							this.inBlockComment = false;
							skipWithNewLines();
							return scanToken();
						}
					} else {
						nextChar();
					}
				}

			} else {
				return(TokenKind.DIVIDE);
			}
			
		// Grouping
		case '(': 
			takeIt();
			return(TokenKind.LPAREN);
		case ')':
			takeIt();
			return(TokenKind.RPAREN);
		case '{':
			takeIt();
			return(TokenKind.LBRACKET);
		case '}':
			takeIt();
			return(TokenKind.RBRACKET);
		case '[':
			takeIt();
			return(TokenKind.LBRACE);
		case ']':
			takeIt();
			return(TokenKind.RBRACE);
			
		// Basic Punctuation
		case '.':
			takeIt();
			return(TokenKind.DOT);
		case ',':
			takeIt();
			return(TokenKind.COMMA);
		case ';':
			takeIt();
			return(TokenKind.SEMICOLON);
		case '0':
			takeIt();
			return (TokenKind.NUM);
		case '1': case '2': case '3': case '4':
		case '5': case '6': case '7': case '8': case '9':
			while (isDigit(currentChar))
				takeIt();
			return(TokenKind.NUM);

		default:
			if (this.inBlockComment || this.inLineComment) {
				return scanToken();
			}else {
				scanError("Unrecognized character '" + currentChar + "' in input");
				return(TokenKind.ERROR);
			}
		}
	}

	private void takeIt() {
		currentSpelling.append(currentChar);
		nextChar();
	}
	

	
	private void skipWithNewLines() {
		while (!eot && (currentChar == ' ' || currentChar == eolWindows || currentChar == eolUnix || currentChar == '\t')) {
			wholeString.append(currentChar);
			skipIt();
		}
	}

	private void skipIt() {
		nextChar();
	}
	
	private TokenKind determineKind(String spelling) {
		switch(spelling) {
		case "if":
			return TokenKind.IF;
		case "else":
			return TokenKind.ELSE;
		case "class":
			return TokenKind.CLASS;
		case "void":
			return TokenKind.VOID;
		case "public":
			return TokenKind.PUBLIC;
		case "private":
			return TokenKind.PRIVATE;
		case "static":
			return TokenKind.STATIC;
		case "int":
			return TokenKind.INT;
		case "boolean":
			return TokenKind.BOOLEAN;
		case "return":
			return TokenKind.RETURN;
		case "true":
			return TokenKind.TRUE;
		case "false":
			return TokenKind.FALSE;
		case "new":
			return TokenKind.NEW;
		case "while":
			return TokenKind.WHILE;
		case "this":
			return TokenKind.THIS;
		case "null":
			return TokenKind.NULL;
		default:
			return TokenKind.IDENTIFIER;
		}
	}

	private boolean isDigit(char c) {
		return (c >= '0') && (c <= '9');
	}
	
	private boolean isLetter(char c) {
		return Character.isLetter(c);
	}
	
	private boolean isLetterDigitUnderscore(char c) {
		boolean isDigit = isDigit(c);
		boolean isLetter = isLetter(c);
		boolean isUnderscore = c == '_';
		return isDigit || isLetter || isUnderscore;
	}

	private void scanError(String m) {
		reporter.reportError("Scan Error:  " + m);
	}


	private final static char eolUnix = '\n';
	private final static char eolWindows = '\r';

	/**
	 * advance to next char in inputstream
	 * detect end of file or end of line as end of input
	 */
	private void nextChar() {
		if (!eot)
			readChar();
	}

	private void readChar() {
		try {
			int c = inputStream.read();
			currentChar = (char) c;
			if (currentChar == eolUnix/* || currentChar == eolWindows*/) {
				lineNumber += 1;
			}
			if (c == -1) {
				eot = true;
			}
		} catch (IOException e) {
			scanError("I/O Exception!");
			eot = true;
		}
	}
}
