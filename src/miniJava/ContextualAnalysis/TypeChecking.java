package miniJava.ContextualAnalysis;

import java.util.HashMap;
import java.util.Map;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.ArrayType;
import miniJava.AbstractSyntaxTrees.AssignStmt;
import miniJava.AbstractSyntaxTrees.BaseType;
import miniJava.AbstractSyntaxTrees.BinaryExpr;
import miniJava.AbstractSyntaxTrees.BlockStmt;
import miniJava.AbstractSyntaxTrees.BooleanLiteral;
import miniJava.AbstractSyntaxTrees.CallExpr;
import miniJava.AbstractSyntaxTrees.CallStmt;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.ClassType;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxRef;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.NewArrayExpr;
import miniJava.AbstractSyntaxTrees.NewObjectExpr;
import miniJava.AbstractSyntaxTrees.NullLiteral;
import miniJava.AbstractSyntaxTrees.NullType;
import miniJava.AbstractSyntaxTrees.Operator;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.ParameterDecl;
import miniJava.AbstractSyntaxTrees.QualRef;
import miniJava.AbstractSyntaxTrees.RefExpr;
import miniJava.AbstractSyntaxTrees.ReturnStmt;
import miniJava.AbstractSyntaxTrees.Statement;
import miniJava.AbstractSyntaxTrees.ThisRef;
import miniJava.AbstractSyntaxTrees.TypeDenoter;
import miniJava.AbstractSyntaxTrees.TypeKind;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenKind;

public class TypeChecking implements Visitor<Object, TypeDenoter>{

	private ErrorReporter reporter;
	private Map<String, ClassDecl> classDecls;
	
	private TypeDenoter currentReturnType;
	
	private boolean hasMain;
	
	public TypeChecking(AST ast, ErrorReporter reporter) {
		this.reporter = reporter;
		hasMain = false;
		classDecls = new HashMap<String, ClassDecl>();
		HashMap<String, Declaration> temp = new IdentificationTable(reporter).getPredefinedDecl();
		currentReturnType = null;
		for (String name: temp.keySet()) {
			classDecls.put(name, (ClassDecl) temp.get(name));
		}
		ast.visit(this,  null);
	}
	
	
	private boolean typeEquality(TypeDenoter a, TypeDenoter b) {
		if (a instanceof ClassType && b instanceof ClassType) {
			return compareClassType(a, b);
		} else if (a instanceof NullType || b instanceof NullType) {
			return compareNullType(a, b);
		} else if (a instanceof BaseType && b instanceof BaseType) {
			return compareBaseType(a, b);
		} else if (a instanceof ArrayType && b instanceof ArrayType) {
			return compareArrayType(a, b);
		}
		return false;
	}
	
	private boolean compareClassType(TypeDenoter a, TypeDenoter b) {
		ClassType aType = (ClassType) a;
		ClassType bType = (ClassType) b;
		if (aType.typeKind == TypeKind.UNSUPPORTED || bType.typeKind == TypeKind.UNSUPPORTED) {
			return false;
		}
		return aType.className.spelling.equals(bType.className.spelling);
	}
	
	private boolean compareNullType(TypeDenoter a, TypeDenoter b) {
		if (a instanceof NullType && b instanceof NullType) {
			return true;
		} else if (a instanceof NullType) {
			// b is something other than NullType
			if (b instanceof ClassType) {
				// need to check if b is unsupported
				ClassDecl bDecl = classDecls.get(((ClassType) b).className.spelling);
				TypeDenoter bDenoter = bDecl.type;
				if (bDenoter != null && bDenoter.typeKind == TypeKind.UNSUPPORTED) {
					return false;
				}
			} else if (b instanceof BaseType){
				return false;
			}
		} else if (b instanceof NullType) {
			if (a instanceof ClassType) {
				ClassDecl aDecl = classDecls.get(((ClassType) a).className.spelling);
				TypeDenoter aDenoter = aDecl.type;
				if (aDenoter != null && aDenoter.typeKind == TypeKind.UNSUPPORTED) {
					return false;
				}
				
			} else if (a instanceof BaseType) {
				return false;
			}
		}
		return true;
		

	}
	
	private boolean compareBaseType(TypeDenoter a, TypeDenoter b) {
		BaseType aType = (BaseType) a;
		BaseType bType = (BaseType) b;
		if (aType.typeKind == TypeKind.ERROR || bType.typeKind == TypeKind.ERROR) {
			return true;
		}
		return aType.typeKind == bType.typeKind;
	}
	
	private boolean compareArrayType(TypeDenoter a, TypeDenoter b) {
		ArrayType aType = (ArrayType) a;
		ArrayType bType = (ArrayType) b;
		return typeEquality(aType.eltType, bType.eltType);
	}
	
	private boolean isRelationalOperator(Operator oper) {
		return oper.kind == TokenKind.LT || oper.kind == TokenKind.LTEQ
				|| oper.kind == TokenKind.GT || oper.kind == TokenKind.GTEQ;
	}
	
	private boolean isEqualityOperator(Operator oper) {
		return oper.kind == TokenKind.EQUALS || oper.kind == TokenKind.NEQ;
	}
	
	private boolean isLogicalOperator(Operator oper) {
		return oper.kind == TokenKind.AND || oper.kind == TokenKind.OR;
	}
	
	private boolean isArithmeticOperator(Operator oper) {
		return oper.kind == TokenKind.PLUS || oper.kind == TokenKind.MINUS
				|| oper.kind == TokenKind.TIMES || oper.kind == TokenKind.DIVIDE;
	}
	
	/*
	 * =======================================================================================
	 *  Start OF DECL (Don't think you have to return anything) (Done)
	 * =======================================================================================
	 */
	@Override
	public TypeDenoter visitPackage(Package prog, Object arg) {
		for (ClassDecl cd: prog.classDeclList) {
			classDecls.put(cd.name, cd);
		}
		
		
		for (ClassDecl cd: prog.classDeclList) {
			cd.visit(this, null);
		}
		
		if (!hasMain) {
			report(prog.posn.start, "Identification", "No main method");
		}
		return null;
	}

	@Override
	public TypeDenoter visitClassDecl(ClassDecl cd, Object arg) {
		for (MethodDecl md: cd.methodDeclList) {
			md.visit(this, null);
		}
		
		for (FieldDecl fd: cd.fieldDeclList) {
			fd.visit(this, null);
		}
		return null;
	}

	@Override
	public TypeDenoter visitFieldDecl(FieldDecl fd, Object arg) {
		return null;
		//return fd.type;
		//fd.type.visit(this, null);
	}

	@Override
	public TypeDenoter visitMethodDecl(MethodDecl md, Object arg) {
		if (md.isStatic && !md.isPrivate && typeEquality(md.type, new BaseType(TypeKind.VOID, null))
				&& md.name.equals("main") && md.parameterDeclList.size() == 1 && md.parameterDeclList.get(0).type instanceof ArrayType) {
			ArrayType temp = (ArrayType) md.parameterDeclList.get(0).type;
			if (typeEquality(temp.eltType, new ClassType(new Identifier(new Token(TokenKind.CLASS, "String", null)), null))) {
				
				if (hasMain) {
					report(md.posn.start, "Identification", "More than 1 main method");
					System.exit(4);
				} else {
					hasMain = true;
				}
			}
		}
		
		
		boolean hasReturn = false;
		currentReturnType = md.type;
		for (ParameterDecl pd: md.parameterDeclList) {
			pd.visit(this, null);
		}
		TypeDenoter returnType = null;
		Statement returnStmt = null;
		for (Statement st: md.statementList) {
			st.visit(this, null);
			returnStmt = st;
		}
		
		if (typeEquality(md.type, new BaseType(TypeKind.VOID, null))) {
			if (!(returnStmt instanceof ReturnStmt)) {
				//System.out.println(returnStmt.posn);
				returnStmt = new ReturnStmt(null, new SourcePosition(md.posn.finish, md.posn.finish));
				md.statementList.add(returnStmt);
			}
		}
		
		if (!(returnStmt instanceof ReturnStmt)) {
			report(returnStmt.posn.start, "Type", "Last statement is not a return statement");
			System.exit(4);
		}
		//if (!hasReturn && !typeEquality(md.type, new BaseType(TypeKind.VOID, null))) {
		//	report(md.posn.start, "Type", "No return statement for a non-void method");
		//}
		currentReturnType = null;
		return null;
	}
	
	

	@Override
	public TypeDenoter visitParameterDecl(ParameterDecl pd, Object arg) {
		return pd.type;
	}

	@Override
	public TypeDenoter visitVarDecl(VarDecl decl, Object arg) {
		return null;
	}
	
	/*
	 * =======================================================================================
	 *  END OF DECL
	 * =======================================================================================
	 */

	
	/*
	 * =======================================================================================
	 *  Start of Type (UNSURE)
	 * =======================================================================================
	 */
	@Override
	public TypeDenoter visitBaseType(BaseType type, Object arg) {
		System.out.println("VISITING BASE TYPE");
		return null;
	}

	@Override
	public TypeDenoter visitClassType(ClassType type, Object arg) {
		System.out.println("VISITING CLASS TYPE");
		return null;
	}

	@Override
	public TypeDenoter visitArrayType(ArrayType type, Object arg) {
		System.out.println("VISITING ARRAY TYPE");
		return null;
	}
	
	/*
	 * =======================================================================================
	 *  END OF Type
	 * =======================================================================================
	 */
	
	/*
	 * =======================================================================================
	 *  Start of Statement (Done)
	 * =======================================================================================
	 */

	@Override
	public TypeDenoter visitBlockStmt(BlockStmt stmt, Object arg) {
		for (Statement st: stmt.sl) {
			st.visit(this, null);
		}
		return null;
	}

	@Override
	public TypeDenoter visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		TypeDenoter varType = stmt.varDecl.type;
		TypeDenoter initType = stmt.initExp.visit(this, null);
		
		if (!typeEquality(varType, initType)) {
			//reporter.reportError("*** Incompatible types in declaration at line: " + stmt.posn.start);
			report(stmt.posn.start, "Type", "Incompatible types in declaration");
		}
		return null;
	}

	@Override
	public TypeDenoter visitAssignStmt(AssignStmt stmt, Object arg) {
		TypeDenoter refType = stmt.ref.visit(this, null);
		//System.out.println("RefType: " + refType);
		//if (refType == null) {
		//	refType = new ClassType(new Identifier(new Token(TokenKind.IDENTIFIER, stmt.ref.decl.name,null)), null);
		//}
		TypeDenoter valType = stmt.val.visit(this, null);
		//System.out.println("ValType: " + valType);
		
		
		if (!typeEquality(refType, valType)) {
			//reporter.reportError("*** Incompatible types in assignment at line: " + stmt.posn.start);
			report(stmt.posn.start, "Type", "Incompatible types in assignment");
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitCallStmt(CallStmt stmt, Object arg) {
		
		MethodDecl md = (MethodDecl) stmt.methodRef.decl;
		if (md.parameterDeclList.size() != stmt.argList.size()) {
			report(stmt.posn.start, "Type", "Incorrect number of parameters provided");
			//reporter.reportError("*** Incorrect number of parameters provided at line: " + stmt.posn.start);
			return null;
		}
		
		for (int i = 0; i< md.parameterDeclList.size(); i++) {
			if (!typeEquality(md.parameterDeclList.get(i).visit(this, null), stmt.argList.get(i).visit(this, null))) {
				report(stmt.argList.get(i).posn.start, "Type", "Incorrect type for parameter in position " + i);
				//reporter.reportError("*** incorrect type for parameter at position " + i + " at line: " + stmt.posn.start);
			}
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitReturnStmt(ReturnStmt stmt, Object arg) {
		
		if (stmt.returnExpr != null) {
			if (!typeEquality(stmt.returnExpr.visit(this, null), currentReturnType)){
				report(stmt.posn.start, "Type", "Invalid return type");
			}
			return null;
		} else {
			if (currentReturnType.typeKind != TypeKind.VOID) {
				report(stmt.posn.start, "Type", "Invalid return type");
			}
			//if (!typeEquality(new BaseType(TypeKind.NULL, null), currentReturnType)) {
			//	report(stmt.posn.start, "Type", "Invalid return type");
			//}
			return null;
		}
	}

	@Override
	public TypeDenoter visitIfStmt(IfStmt stmt, Object arg) {
		TypeDenoter condType = stmt.cond.visit(this, null);
		if (condType instanceof BaseType && condType.typeKind == TypeKind.BOOLEAN) {
			
		} else {
			report(stmt.cond.posn.start, "Type", "Condition must be of type BOOLEAN");
			//reporter.reportError("*** If condition must be of type boolean, line: " + stmt.cond.posn.start);
		}
		stmt.thenStmt.visit(this, null);
		if (stmt.elseStmt != null) {
			stmt.elseStmt.visit(this, null);
		}
		return null;
	}

	@Override
	public TypeDenoter visitWhileStmt(WhileStmt stmt, Object arg) {
		
		TypeDenoter condType = stmt.cond.visit(this, null);
		if (condType instanceof BaseType && condType.typeKind == TypeKind.BOOLEAN) {
			
		} else {
			report(stmt.cond.posn.start, "Type", "Condition must be of type BOOLEAN");
			//reporter.reportError("*** While condition must be of type boolean, line: " + stmt.cond.posn.start);
		}
		
		stmt.body.visit(this, null);
		return null;
	}
	
	/*
	 * =======================================================================================
	 *  END OF STATEMENT
	 * =======================================================================================
	 */

	/*
	 * =======================================================================================
	 *  Start of Expression
	 * =======================================================================================
	 */
	@Override
	public TypeDenoter visitUnaryExpr(UnaryExpr expr, Object arg) {
		if (expr.operator.kind == TokenKind.NOT) {
			TypeDenoter temp = expr.expr.visit(this, null);
			if (temp instanceof BaseType && temp.typeKind == TypeKind.BOOLEAN) {
				expr.type = temp;
				return temp;
			} else {
				report(expr.posn.start, "Type", "Unary operator NOT can only be applied to type BOOLEAN");
				//reporter.reportError("*** Unary operator NOT can only be applied to a boolean, line: " + expr.posn.start);
				//System.exit(4);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
		} else if (expr.operator.kind == TokenKind.MINUS) {
			TypeDenoter temp = expr.expr.visit(this, null);
			if (temp instanceof BaseType && temp.typeKind == TypeKind.INT) {
				expr.type = temp;
				return expr.expr.visit(this, null);
			} else {
				report(expr.posn.start, "Type", "Unary operator MINUS can only be applied to type INTEGER");
				//reporter.reportError("*** Unary operator MINUS can only be applied to a boolean, line: " + expr.posn.start);
				//System.exit(4);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
		} else {
			report(expr.posn.start, "Type", "Invalid unary operator (" + expr.operator.spelling + ")");
			//reporter.reportError("*** Invalid unary operator (" + expr.operator.spelling + ") at line: " + expr.posn.start);
			//System.exit(4);
			expr.type = new BaseType(TypeKind.ERROR, null);
			return new BaseType(TypeKind.ERROR, null);
		}
	}

	@Override
	public TypeDenoter visitBinaryExpr(BinaryExpr expr, Object arg) {
		TypeDenoter leftType = expr.left.visit(this, null);
		TypeDenoter rightType = expr.right.visit(this, null);
		
		if (isRelationalOperator(expr.operator)) {
			
			if (! (leftType instanceof BaseType) || leftType.typeKind != TypeKind.INT) {
				report(expr.left.posn.start, "Type", "Relational operators can only be applied to type INT");
				//reporter.reportError("*** Relational operators can only be applied to integers, line: " + expr.left.posn.start);;
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			if (! (rightType instanceof BaseType) || rightType.typeKind != TypeKind.INT) {
				report(expr.right.posn.start, "Type", "Relational operators can only be applied to type INT");
				//reporter.reportError("*** Relational operators can only be applied to integers, line: " + expr.right.posn.start);;
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			expr.type = new BaseType(TypeKind.BOOLEAN, null);
			return new BaseType(TypeKind.BOOLEAN, null);
		} else if (isEqualityOperator(expr.operator)) {
			if (!typeEquality(leftType, rightType)) {
				report(expr.operator.posn.start, "Type", "Comparison of uncomparable types");
				//reporter.reportError("*** Comparison of uncomparable types, line: " + expr.operator.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			} else {
				expr.type = new BaseType(TypeKind.BOOLEAN, null);
				return new BaseType(TypeKind.BOOLEAN, null);
			}
		} else if (isLogicalOperator(expr.operator)) {
			if (!(leftType instanceof BaseType) || leftType.typeKind != TypeKind.BOOLEAN) {
				report(expr.operator.posn.start, "Type", "Logical operator can only be applied to type BOOLEAN");
				//reporter.reportError("*** Logical operator can only be applied to booleans, line: " + expr.operator.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			if (!(rightType instanceof BaseType) || rightType.typeKind != TypeKind.BOOLEAN) {
				report(expr.operator.posn.start, "Type", "Logical operator can only be applied to type BOOLEAN");
				//reporter.reportError("*** Logical operator can only be applied to booleans, line: " + expr.operator.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			expr.type = new BaseType(TypeKind.BOOLEAN, null);
			return new BaseType(TypeKind.BOOLEAN, null);
		} else if (isArithmeticOperator(expr.operator)) {
			if (!(leftType instanceof BaseType) || leftType.typeKind != TypeKind.INT) {
				report(expr.operator.posn.start, "Type", "Arithmetic operator can only be applied to type INT");
				//reporter.reportError("*** Arithmetic operator can only be applied to integers, line: " + expr.operator.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			if (!(rightType instanceof BaseType) || rightType.typeKind != TypeKind.INT) {
				report(expr.operator.posn.start, "Type", "Arithmetic operator can only be applied to type INT");
				//reporter.reportError("*** Arithmetic operator can only be applied to integers, line: " + expr.operator.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
			expr.type = new BaseType(TypeKind.INT, null);
			return new BaseType(TypeKind.INT, null);
		} 
		expr.type = new BaseType(TypeKind.ERROR, null);
		return new BaseType(TypeKind.ERROR, null);
	}
	

	@Override
	public TypeDenoter visitRefExpr(RefExpr expr, Object arg) {
		
		TypeDenoter temp = expr.ref.visit(this, null);
		expr.type = temp;
		return temp;
		
	}

	@Override
	public TypeDenoter visitCallExpr(CallExpr expr, Object arg) {
		
		MethodDecl md = (MethodDecl) expr.functionRef.decl;
		if (md.parameterDeclList.size() != expr.argList.size()) {
			report(expr.posn.start, "Type", "Incorrect number of parameters provided");
			//reporter.reportError("*** Incorrect number of parameters provided at line: " + expr.posn.start);
			expr.type = new BaseType(TypeKind.ERROR, null);
			return new BaseType(TypeKind.ERROR, null);
		}
		
		for (int i = 0; i< md.parameterDeclList.size(); i++) {
			if (! typeEquality(md.parameterDeclList.get(i).visit(this, null), expr.argList.get(i).visit(this, null))) {
				report(expr.argList.get(i).posn.start, "Type", "Incorrect type for parameter in position " + i);
				//reporter.reportError("*** incorrect type for parameter at position " + i + " at line: " + expr.posn.start);
				expr.type = new BaseType(TypeKind.ERROR, null);
				return new BaseType(TypeKind.ERROR, null);
			}
		}
		
		expr.type = md.type;
		return md.type;
	}

	@Override
	public TypeDenoter visitLiteralExpr(LiteralExpr expr, Object arg) {
		TypeDenoter temp =  expr.lit.visit(this, null);
		expr.type = temp;
		return temp;
	}

	@Override
	public TypeDenoter visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		
		TypeDenoter temp = expr.classtype;
		expr.type = temp;
		return temp;
	}

	@Override
	public TypeDenoter visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		
		TypeDenoter sizeType = expr.sizeExpr.visit(this, null);
		if (sizeType instanceof BaseType && sizeType.typeKind == TypeKind.INT) {
			
		} else {
			report(expr.sizeExpr.posn.start, "Type", "Array size expression must be of type INT");
			//reporter.reportError("*** Invalid type for array size expression, line: " + expr.sizeExpr.posn.start);
			expr.type = new BaseType(TypeKind.ERROR, null);
			return new BaseType(TypeKind.ERROR, null);
		}
		expr.type = new ArrayType(expr.eltType, null);
		return new ArrayType(expr.eltType, null);
	}
	
	/*
	 * =======================================================================================
	 *  END OF EXPRESSION
	 * =======================================================================================
	 */

	@Override
	public TypeDenoter visitThisRef(ThisRef ref, Object arg) {
		//System.out.println("IN VISIT THIS REF IN TYPE");
		TypeDenoter temp = new ClassType(new Identifier(new Token(TokenKind.IDENTIFIER, ref.decl.name,null)), null);
		//System.out.println(temp);
		return temp;
		//return null;
	}

	@Override
	public TypeDenoter visitIdRef(IdRef ref, Object arg) {
		return ref.id.decl.type;
	}

	@Override
	public TypeDenoter visitQRef(QualRef ref, Object arg) {
		return ref.id.decl.type;
	}

	@Override
	//TODO set type
	public TypeDenoter visitIxRef(IxRef ref, Object arg) {
		if (ref.indexExpr.visit(this, null).typeKind != TypeKind.INT) {
			report(ref.posn.start, "Type", "Array index expression must be of type INT");
			//reporter.reportError("*** Array index expression must be of type INT, line: " + ref.posn.start);
			return new BaseType(TypeKind.ERROR, null);
		}
		try {
			ArrayType aType = (ArrayType) ref.ref.visit(this, null);
			return aType.eltType;
		} catch (Exception e) {
			report(ref.posn.start, "Type", "Cannot perform indexing on a non-array type");
			return new BaseType(TypeKind.ERROR, null);
		}
	}

	@Override
	public TypeDenoter visitIdentifier(Identifier id, Object arg) {
		return id.decl.type;
	}

	/*
	 * Don't think I need to modify this
	 */
	@Override
	public TypeDenoter visitOperator(Operator op, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitIntLiteral(IntLiteral num, Object arg) {
		return new BaseType(TypeKind.INT, null);
	}

	@Override
	public TypeDenoter visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		return new BaseType(TypeKind.BOOLEAN, null);
	}

	@Override
	public TypeDenoter visitNullLiteral(NullLiteral nullLit, Object arg) {
		return new NullType(null);
	}


	@Override
	public TypeDenoter visitNullType(NullType type, Object arg) {
		return null;
	}
	
	private void report(int line, String type, String reason) {
		reporter.reportError("*** line " + line + ":  " + type + " error - " + reason);
	}


	

}
