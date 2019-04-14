/**
 * Parser
 *
 * Grammar:
 *   S ::= E '$'
 *   E ::= T (oper T)*     
 *   T ::= num | '(' E ')'
 */
package miniJava.SyntacticAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.TokenKind;

public class Parser {

	private Scanner scanner;
	private ErrorReporter reporter;
	private Token token;
	private boolean trace = true;
	
	private SourcePosition previousTokenPosition;

	public Parser(Scanner scanner, ErrorReporter reporter) {
		this.scanner = scanner;
		this.reporter = reporter;
		this.previousTokenPosition = new SourcePosition();
	}


	/**
	 * SyntaxError is used to unwind parse stack when parse fails
	 *
	 */
	class SyntaxError extends Error {
		private static final long serialVersionUID = 1L;	
	}
	
	private void start(SourcePosition posn) {
		posn.start = token.posn.start;
	}
	
	private void end(SourcePosition posn) {
		posn.finish = previousTokenPosition.finish;
	}

	
	/*
	 * DONE
	 */
	public Package parse() {
		previousTokenPosition.start = 1;
	    previousTokenPosition.finish = 1;
	    
	    int start = 1;
	    
		token = scanner.scan();
		try {
			ClassDeclList temp = parseProgram();
			return new Package(temp, new SourcePosition(start, previousTokenPosition.finish));
			//return new Package(parseProgram(), previousTokenPosition);
		}
		catch (SyntaxError e) { 
			return null;
		}
	}
	
	/*
	 * DONE
	 */
	private ClassDeclList parseProgram() throws SyntaxError{
		ClassDeclList list = new ClassDeclList();
		while(token.kind == TokenKind.CLASS) {
			list.add(parseClassDeclaration());
		}
		accept(TokenKind.EOT);
		return list;
	}
	
	/*
	 * DONE
	 */
	private ClassDecl parseClassDeclaration() throws SyntaxError{
		SourcePosition classPos = new SourcePosition();
		start(classPos);
		accept(TokenKind.CLASS);
		
		//System.out.println(token.kind);
		String className = token.spelling;
		accept(TokenKind.IDENTIFIER);
		accept(TokenKind.LBRACKET);
		
		
		FieldDeclList fdl = new FieldDeclList();
		MethodDeclList mdl = new MethodDeclList();
		
		ParameterDeclList pdl;
		FieldDecl fd;
		
		// For the kleene star
		while(token.kind == TokenKind.PUBLIC ||
				token.kind == TokenKind.PRIVATE ||
				token.kind == TokenKind.STATIC ||
				token.kind == TokenKind.VOID ||
				isTypeRule(token)) {
			SourcePosition fieldPos = new SourcePosition();
			SourcePosition memberPos = new SourcePosition();
	
			start(fieldPos);
			start(memberPos);
			
			boolean isPrivate = false;
			boolean isStatic = false;
			switch (token.kind) {
				// Access modifiers
				case PUBLIC: case PRIVATE: case STATIC:
					if (token.kind == TokenKind.PRIVATE) {
						isPrivate = true;
					}
					if (token.kind == TokenKind.STATIC) {
						isStatic = true;
					}
					acceptIt();
					if (token.kind == TokenKind.STATIC) {
						acceptIt();
						isStatic = true;
					}
				default:
					// Could be field or method
					if (isTypeRule(token)) {
						TypeDenoter typeDenoter = parseType();
						String name;
						name = token.spelling;
						
						accept(TokenKind.IDENTIFIER);
						
						if (token.kind == TokenKind.SEMICOLON) {
							// Field Declaration
							end(fieldPos);
							acceptIt();
							
							fdl.add(new FieldDecl(isPrivate, isStatic, typeDenoter, name, fieldPos));
						} else {
							// Method Declaration (w return type)
							StatementList list = new StatementList();
							accept(TokenKind.LPAREN);
							pdl = parseParameterList();
							end(fieldPos);
							fd = new FieldDecl(isPrivate, isStatic, typeDenoter, name, fieldPos);
							accept(TokenKind.RPAREN);
							accept(TokenKind.LBRACKET);
							while (token.kind != TokenKind.RBRACKET) {
								list.add(parseStatement());
							}
							end(memberPos);
							accept(TokenKind.RBRACKET);
							
							mdl.add(new MethodDecl(fd, pdl, list, memberPos));
							break;
						}
					} else if (token.kind == TokenKind.VOID) {
						// Method Declaration (without return type)
						SourcePosition temp = new SourcePosition();
						start(temp);
						StatementList list = new StatementList();
						acceptIt();
						end(temp);
						String name = token.spelling;
						accept(TokenKind.IDENTIFIER);
						accept(TokenKind.LPAREN);
						pdl = parseParameterList();
						end(fieldPos);
						fd = new FieldDecl(isPrivate, isStatic, new BaseType(TypeKind.VOID, temp), name, fieldPos);
						accept(TokenKind.RPAREN);
						
						accept(TokenKind.LBRACKET);
						
						while (token.kind != TokenKind.RBRACKET) {
							list.add(parseStatement());
						}
						end(memberPos);
						accept(TokenKind.RBRACKET);
						
						mdl.add(new MethodDecl(fd, pdl, list, memberPos));
						break;
					} else {
						parseError("Expecting a type or void but found: " + token.kind);
					}
				
			}
				
		}
		Token rBrack = token;
		accept(TokenKind.RBRACKET);
		
		end(classPos);
		classPos.finish = rBrack.posn.start;
		
		return new ClassDecl(className, fdl, mdl, classPos);
		
	}
	
	// Helper method to see if it is possible to parse a type
	private boolean isTypeRule(Token token) {
		return (token.kind == TokenKind.IDENTIFIER || 
				token.kind == TokenKind.INT ||
				token.kind == TokenKind.BOOLEAN);
		
	}
	
	
	/*
	 * DONE
	 */
	private TypeDenoter parseType() throws SyntaxError{
		TypeDenoter test;
		SourcePosition temp = new SourcePosition();
		SourcePosition array = new SourcePosition();
		start(temp);
		start(array);
		switch (token.kind) {
		case BOOLEAN:
			acceptIt();
			end(temp);
			test = new BaseType(TypeKind.BOOLEAN, temp);
			return test;
		case INT:
			acceptIt();
			end(temp);
			if (token.kind == TokenKind.LBRACE) {
				acceptIt();
				accept(TokenKind.RBRACE);
				end(array);
				test = new ArrayType(new BaseType(TypeKind.INT, temp), array);
				return test;
			} else {
				return new BaseType(TypeKind.INT, temp);
			}
		case IDENTIFIER:
			Token type = token;
			acceptIt();
			end(temp);
			if (token.kind == TokenKind.LBRACE) {
				acceptIt();
				accept(TokenKind.RBRACE);
				end(array);
				test = new ArrayType(new ClassType(new Identifier(type), temp), array);
				return test;
			} else {
				return new ClassType(new Identifier(type), temp);
			}
		default:
			parseError("Expecting Boolean, Num, or Identifier but found: " + token.kind);
			return null;
		}
	}
	
	/*
	 * DONE
	 */
	private ParameterDeclList parseParameterList() throws SyntaxError{
		ParameterDeclList list = new ParameterDeclList();
		if (token.kind == TokenKind.RPAREN) {
			// No parameters
			return list;
		}
		SourcePosition parameterPos = new SourcePosition();
		start(parameterPos);
		TypeDenoter denoter = parseType();
		String name = token.spelling;
		end(parameterPos);
		list.add(new ParameterDecl(denoter, name, parameterPos));
		accept(TokenKind.IDENTIFIER);
		while (token.kind == TokenKind.COMMA) {
			acceptIt();
			start(parameterPos);
			denoter = parseType();
			name = token.spelling;
			end(parameterPos);
			list.add(new ParameterDecl(denoter, name, parameterPos));
			accept(TokenKind.IDENTIFIER);
		}
		return list;

	}
	
	/*
	 * DONE
	 */
	private ExprList parseArgumentList() throws SyntaxError{
		ExprList list = new ExprList();
		if (token.kind == TokenKind.RPAREN) {
			// No arguments
			return list;
		}
		list.add(parseExpression());
		while (token.kind == TokenKind.COMMA) {
			acceptIt();
			list.add(parseExpression());
		}
		return list;
	}
	
	/*
	 * DONE
	 */
	private Reference parseReference() throws SyntaxError{
		Reference ref = null;
		SourcePosition refPos = new SourcePosition();
		if (token.kind == TokenKind.THIS) {
			start(refPos);
			acceptIt();
			end(refPos);
			ref = new ThisRef(refPos);
		} else if (token.kind == TokenKind.IDENTIFIER) {
			Token id = token;
			start(refPos);
			acceptIt();
			end(refPos);
			ref = new IdRef(new Identifier(id), refPos);
		} else {
			parseError("Expecting this or an identifier but found: " + token.kind);
			return null;
		}
		while (token.kind == TokenKind.DOT) {
			//SourcePosition qualRefPos = new SourcePosition();
			int start = refPos.start;
			// For nested de-references
			acceptIt();
			//end(qualRefPos);
			Token t = token;
			accept(TokenKind.IDENTIFIER);
			int end = t.posn.finish;
			ref = new QualRef(ref, new Identifier(t), new SourcePosition(start, end));
			refPos = new SourcePosition(start, end);
		}
		return ref;


	}
	

	
	/*
	 * DONE
	 */
	private Statement parseStatement() throws SyntaxError{
		Expression exp;
		String name;
		Statement stmt;
		
		
		SourcePosition stmtPos = new SourcePosition();
		
		SourcePosition typePos = new SourcePosition();
		SourcePosition arrayPos = new SourcePosition();
		SourcePosition vDeclPos = new SourcePosition();
		SourcePosition refPos = new SourcePosition();
		
		start(stmtPos);
		start(vDeclPos);
		switch (token.kind) {
		case LBRACKET:
			SourcePosition blockPos = new SourcePosition();
			start(blockPos);
			acceptIt();
			StatementList list = new StatementList();
			while (token.kind != TokenKind.RBRACKET) {
				list.add(parseStatement());
			}
			end(blockPos);
			acceptIt();
			//end(blockPos);
			return new BlockStmt(list, blockPos);
		case BOOLEAN:
			//typePosition = token;
			start(typePos);
			acceptIt();
			end(typePos);
			name = token.spelling;
			accept(TokenKind.IDENTIFIER);
			end(vDeclPos);
			VarDecl vDecl = new VarDecl(new BaseType(TypeKind.BOOLEAN, typePos), name, vDeclPos);
			
			accept(TokenKind.ASSIGNMENT);
			exp = parseExpression();
			end(stmtPos);
			accept(TokenKind.SEMICOLON);
			return new VarDeclStmt(vDecl, exp, stmtPos);
		case INT:
			//typePosition = token;
			start(typePos);
			start(arrayPos);
			acceptIt();
			end(typePos);
			TypeDenoter type;
			if (token.kind == TokenKind.LBRACE) {
				acceptIt();
				accept(TokenKind.RBRACE);
				end(arrayPos);
				type = new ArrayType(new BaseType(TypeKind.INT, typePos), arrayPos);
			} else {
				type = new BaseType(TypeKind.INT, typePos);
			}
			name = token.spelling;
			accept(TokenKind.IDENTIFIER);
			end(vDeclPos);
			accept(TokenKind.ASSIGNMENT);
			exp = parseExpression();
			end(stmtPos);
			accept(TokenKind.SEMICOLON);
			return new VarDeclStmt(new VarDecl(type, name, vDeclPos), exp, stmtPos);
		case THIS:
			Reference ref = parseReference();
			stmt = parseReferenceHelper(ref, stmtPos);
			accept(TokenKind.SEMICOLON);
			end(stmtPos);
			return stmt;
		case RETURN:
			acceptIt();
			if (token.kind == TokenKind.SEMICOLON) {
				end(stmtPos);
				acceptIt();
				return new ReturnStmt(null, stmtPos);
			} else {
				exp = parseExpression();
				end(stmtPos);
				accept(TokenKind.SEMICOLON);
				return new ReturnStmt(exp, stmtPos);
			}
		case IF:
			IfStmt ifStmt;
			acceptIt();
			accept(TokenKind.LPAREN);
			exp = parseExpression();
			accept(TokenKind.RPAREN);
			stmt = parseStatement();
			if (token.kind == TokenKind.ELSE) {
				acceptIt();
				Statement additional = parseStatement();
				end(stmtPos);
				ifStmt = new IfStmt(exp, stmt, additional, stmtPos);
			} else {
				end(stmtPos);
				ifStmt = new IfStmt(exp, stmt, stmtPos);
			}
			return ifStmt;
		case WHILE:
			acceptIt();
			accept(TokenKind.LPAREN);
			exp = parseExpression();
			accept(TokenKind.RPAREN);
			stmt = parseStatement();
			end(stmtPos);
			stmtPos.finish = stmtPos.finish - 1;
			return new WhileStmt(exp, stmt, stmtPos);
		case IDENTIFIER:
			Token currentToken = token;
			start(typePos);
			start(arrayPos);
			start(vDeclPos);
			start(refPos);
			acceptIt();
			end(typePos);
			switch (token.kind) {
			case LBRACE:
				acceptIt();
				if (token.kind == TokenKind.RBRACE) {
					//Type (id[]) id = Expression
					accept(TokenKind.RBRACE);
					end(arrayPos);
					Token identifier = token;
					accept(TokenKind.IDENTIFIER);
					end(vDeclPos);
					accept(TokenKind.ASSIGNMENT);
					exp = parseExpression();
					ArrayType cType = new ArrayType(new ClassType(new Identifier(currentToken), typePos), arrayPos);
					VarDecl vDeclaration = new VarDecl(cType, identifier.spelling, vDeclPos);
					end(stmtPos);
					accept(TokenKind.SEMICOLON);
					return new VarDeclStmt(vDeclaration, exp, stmtPos);
				} else {
					//IxReference = Expression
					Expression index = parseExpression();
					end(refPos);
					SourcePosition temp = new SourcePosition(refPos.start, typePos.finish);
					IxRef ixRef = new IxRef(new IdRef(new Identifier(currentToken), temp), index, refPos);
					accept(TokenKind.RBRACE);
					accept(TokenKind.ASSIGNMENT);
					exp = parseExpression();
					end(stmtPos);
					accept(TokenKind.SEMICOLON);
					return new AssignStmt(ixRef, exp, stmtPos);
				}
			case IDENTIFIER:
				// Type id = expression
				String spelling = token.spelling;
				ClassType classType = new ClassType(new Identifier(currentToken), typePos);
				acceptIt();
				VarDecl varDecl = new VarDecl(classType, spelling, vDeclPos);
				end(vDeclPos);
				accept(TokenKind.ASSIGNMENT);
				exp = parseExpression();
				end(stmtPos);
				accept(TokenKind.SEMICOLON);
				return new VarDeclStmt(varDecl, exp, stmtPos);
			default:
				//id(.id)*
				end(refPos);
				ref = new IdRef(new Identifier(currentToken), refPos);
				while (token.kind == TokenKind.DOT) {
					int start = refPos.start;
					acceptIt();
					Token t = token;
					accept(TokenKind.IDENTIFIER);
					int end = t.posn.finish;
					ref = new QualRef(ref, new Identifier(t), new SourcePosition(start, end));
					refPos = new SourcePosition(start, end);
				}
				stmt = parseReferenceHelper(ref, stmtPos);
				accept(TokenKind.SEMICOLON);
				end(stmtPos);
				return stmt;
			}
		default:
			parseError("Expecting {, boolean, num, this, return, if, while, identifier, or . but found: " + token.kind);
			return null;
		}
	}
	
	/*
	 * DONE
	 */
	// Helper method for parsing complicated statements
	private Statement parseReferenceHelper(Reference ref, SourcePosition stmt) throws SyntaxError{
		Expression exp;
		Expression assn;
		SourcePosition temp = new SourcePosition();
		temp.start = ref.posn.start;
		//Statement stmt;
		switch (token.kind) {
		case LBRACE:
			// Indexed Reference
			acceptIt();
			exp = parseExpression();
			end(temp);
			IxRef ixRef = new IxRef(ref, exp, temp);
			accept(TokenKind.RBRACE);
			accept(TokenKind.ASSIGNMENT);
			assn = parseExpression();
			end(stmt);
			return new AssignStmt(ixRef, assn, stmt);
		case ASSIGNMENT:
			// Reference
			acceptIt();
			assn = parseExpression();
			end(stmt);
			return new AssignStmt(ref, assn, stmt);
		case LPAREN:
			// Method Call
			acceptIt();
			ExprList list = parseArgumentList();
			accept(TokenKind.RPAREN);
			end(stmt);
			return new CallStmt(ref, list, stmt);
		default:
			parseError("Expecting [, =, or (");
			return null;
		}
	}
	
	/*
	 * Need to deal with the whole parseBinop thing
	 */
	private Expression parseExpression() throws SyntaxError{
		return parseA();
	}

	// For Expression binop Expression
	private Expression parseExpressionHelper() throws SyntaxError{
		Expression exp;
		Expression tempExp;
		Token idToken;
		Token tempToken;
		Reference ref;
		
		SourcePosition newExprPos = new SourcePosition();
		SourcePosition classPos = new SourcePosition();
		SourcePosition exprPos = new SourcePosition();
		
		switch (token.kind) {
		case TRUE: case FALSE:
			SourcePosition boolPos = new SourcePosition();
			start(boolPos);
			Token currentToken = token;
			acceptIt();
			end(boolPos);
			exp = new LiteralExpr(new BooleanLiteral(currentToken), boolPos);
			return exp;
		case NULL:
			SourcePosition nullPos = new SourcePosition();
			start(nullPos);
			currentToken = token;
			acceptIt();
			end(nullPos);
			exp = new LiteralExpr(new NullLiteral(currentToken), nullPos);
			return exp;
		// Final rule of the grammar
		case NEW:
			start(newExprPos);
			acceptIt();
			switch (token.kind) {
			// new id() || new int[Expression]
			case IDENTIFIER:
				idToken = token;
				start(classPos);
				acceptIt();
				end(classPos);
				if (token.kind == TokenKind.LPAREN) {
					// new id()
					acceptIt();
					accept(TokenKind.RPAREN);
					end(newExprPos);
					exp = new NewObjectExpr(new ClassType(new Identifier(idToken), classPos), newExprPos);
					return exp;
				} else {
					// new id[Expression]
					accept(TokenKind.LBRACE);
					tempExp = parseExpression();
					accept(TokenKind.RBRACE);
					end(newExprPos);
					exp = new NewArrayExpr(new ClassType(new Identifier(idToken), classPos), tempExp, newExprPos);
					return exp;
				}
			// new int[Expression]
			case INT:
				SourcePosition intPos = new SourcePosition();
				start(intPos);
				acceptIt();
				end(intPos);
				accept(TokenKind.LBRACE);
				tempExp = parseExpression();
				accept(TokenKind.RBRACE);
				end(newExprPos);
				exp = new NewArrayExpr(new BaseType(TypeKind.INT, intPos), tempExp, newExprPos);
				return exp;
			default:
				parseError("Expecting an identifier or a num but found: " + token.kind);
			}
		case NUM:
			Token temp = token;
			SourcePosition intPos = new SourcePosition();
			start(intPos);
			acceptIt();
			end(intPos);
			exp = new LiteralExpr(new IntLiteral(temp), intPos);
			return exp;
		// (Expression)
		case LPAREN:
			acceptIt();
			exp = parseExpression();
			accept(TokenKind.RPAREN);
			return exp;
		// Unop Expression
		case NOT: case MINUS:
			start(exprPos);
			tempToken = token;
			acceptIt();
			tempExp = parseExpression();
			end(exprPos);
			exp = new UnaryExpr(new Operator(tempToken), tempExp, exprPos);
			return exp;
			
		default:
			SourcePosition defaultPos = new SourcePosition();
			SourcePosition refPos = new SourcePosition();
			start(refPos);
			start(defaultPos);
			ref = parseReference();
			switch (token.kind) {
			case LPAREN:
				// Reference (arg list)
				acceptIt();
				ExprList exprList = parseArgumentList();
				accept(TokenKind.RPAREN);
				end(defaultPos);
				exp = new CallExpr(ref, exprList, defaultPos);
				return exp;
			case LBRACE:
				// Reference [index]
				acceptIt();
				tempExp = parseExpression();
				end(refPos);
				IxRef ixRef = new IxRef(ref, tempExp, refPos);
				accept(TokenKind.RBRACE);
				end(defaultPos);
				exp = new RefExpr(ixRef, defaultPos);
				return exp;
			default:
				//Reference
				end(defaultPos);
				exp = new RefExpr(ref, defaultPos);
				return exp;
			}
		}
	}
	
	
	/*
	 * -----------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------
	 * |||                                                                       |||
	 * |||           Helper methods for enforcing a stratified grammar           |||
	 * |||                                                                       |||
	 * -----------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------
	 */
	private Expression parseA() throws SyntaxError{
		SourcePosition aPos = new SourcePosition();
		//SourcePosition temp = new SourcePosition();
		start(aPos);
		Expression expr = parseB();
		while (token.kind == TokenKind.OR) {
			aPos = new SourcePosition(aPos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseB();
			end(aPos);
			expr = new BinaryExpr(oper, expr, expr2, aPos);
		}
		return expr;
	}
	
	private Expression parseB() throws SyntaxError{
		SourcePosition bPos = new SourcePosition();
		start(bPos);
		Expression expr = parseC();
		while (token.kind == TokenKind.AND) {
			bPos = new SourcePosition(bPos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseC();
			end(bPos);
			expr = new BinaryExpr(oper, expr, expr2, bPos);
		}
		return expr;
	}

	private Expression parseC() throws SyntaxError{
		SourcePosition cPos = new SourcePosition();
		start(cPos);
		Expression expr = parseD();
		while (token.kind == TokenKind.EQUALS || token.kind == TokenKind.NEQ) {
			cPos = new SourcePosition(cPos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseD();
			end(cPos);
			expr = new BinaryExpr(oper, expr, expr2, cPos);
		}
		return expr;
	}

	private Expression parseD() throws SyntaxError{
		SourcePosition dPos = new SourcePosition();
		start(dPos);
		Expression expr = parseE();
		while (token.kind == TokenKind.LT || token.kind == TokenKind.LTEQ
				|| token.kind == TokenKind.GT || token.kind == TokenKind.GTEQ) {
			dPos = new SourcePosition(dPos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseE();
			end(dPos);
			expr = new BinaryExpr(oper, expr, expr2, dPos);
		}
		return expr;
	}

	private Expression parseE() throws SyntaxError{
		SourcePosition ePos = new SourcePosition();
		start(ePos);
		Expression expr = parseF();
		while (token.kind == TokenKind.PLUS || token.kind == TokenKind.MINUS) {
			ePos = new SourcePosition(ePos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseF();
			end(ePos);
			expr = new BinaryExpr(oper, expr, expr2, ePos);
		}
		return expr;
	}

	private Expression parseF() throws SyntaxError{
		SourcePosition fPos = new SourcePosition();
		start(fPos);
		Expression expr = parseG();
		while (token.kind == TokenKind.TIMES || token.kind == TokenKind.DIVIDE) {
			fPos = new SourcePosition(fPos.start, 0);
			Operator oper = new Operator(token);
			acceptIt();
			Expression expr2 = parseG();
			end(fPos);
			expr = new BinaryExpr(oper, expr, expr2, fPos);
		}
		return expr;
	}

	
	private Expression parseG() throws SyntaxError{
		SourcePosition gPos = new SourcePosition();
		start(gPos);
		switch (token.kind) {
		case MINUS: case NOT:
			Operator oper = new Operator(token);
			acceptIt();
			end(gPos);
			return new UnaryExpr(oper, parseG(), gPos);
		default:
			return parseH();
		}
	}
	
	private Expression parseH() throws SyntaxError{
		switch (token.kind) {
		case LPAREN:
			acceptIt();
			Expression exp = parseA();
			accept(TokenKind.RPAREN);
			return exp;
		default:
			return parseExpressionHelper();
		}
	}
	
	// Helper method to check if a token is binop
	private boolean isBinop() {
		boolean result = token.kind == TokenKind.PLUS ||
				token.kind == TokenKind.MINUS ||
				token.kind == TokenKind.DIVIDE ||
				token.kind == TokenKind.TIMES ||
				token.kind == TokenKind.GT ||
				token.kind == TokenKind.GTEQ ||
				token.kind == TokenKind.LT ||
				token.kind == TokenKind.LTEQ ||
				token.kind == TokenKind.EQUALS ||
				token.kind == TokenKind.NEQ ||
				token.kind == TokenKind.AND ||
				token.kind == TokenKind.OR;
		return result;
	}


	/**
	 * accept current token and advance to next token
	 */
	private void acceptIt() throws SyntaxError {
		accept(token.kind);
	}

	/**
	 * verify that current token in input matches expected token and advance to next token
	 * @param expectedToken
	 * @throws SyntaxError  if match fails
	 */
	private void accept(TokenKind expectedTokenKind) throws SyntaxError {
		if (token.kind == expectedTokenKind) {
			if (trace)
				pTrace();
			previousTokenPosition = token.posn;
			token = scanner.scan();
		}
		else
			parseError("expecting '" + expectedTokenKind +
					"' but found '" + token.kind + "'");
	}

	/**
	 * report parse error and unwind call stack to start of parse
	 * @param e  string with error detail
	 * @throws SyntaxError
	 */
	private void parseError(String e) throws SyntaxError {
		reporter.reportError("Parse error: " + e);
		throw new SyntaxError();
	}

	// show parse stack whenever terminal is  accepted
	private void pTrace() {
		return;
		/*
		StackTraceElement [] stl = Thread.currentThread().getStackTrace();
		for (int i = stl.length - 1; i > 0 ; i--) {
			if(stl[i].toString().contains("parse"))
				System.out.println(stl[i]);
		}
		System.out.println("accepting: " + token.kind + " (\"" + token.spelling + "\")");
		System.out.println();
		*/
	}
	

}
