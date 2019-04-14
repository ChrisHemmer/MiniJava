package miniJava.CodeGenerator;

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
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;

import mJAM.Machine;
import mJAM.Machine.*;
import mJAM.ObjectFile;
import mJAM.Instruction;

public class CodeGenerator implements Visitor<Object, Object>{
	/*
	 * Need to visit all decls and generate runtime entities for them
	 * 
	 * Decls dont deal with frames?
	 */
	
	int mainMethodAddr = 0;
	
	int varDeclOffset = 0;
	MethodDecl currMethod;
	
	
	public void encodeRun(AST ast) {
		ast.visit(this, null);
	}
	
	@Override
	public Object visitPackage(Package prog, Object arg) {
		/* Preamble */
		Machine.emit(Op.LOADL, 0);
		Machine.emit(Prim.newarr);
		Machine.emit(Op.CALL, 0, Reg.CB, 1);
		int tempAddr = Machine.nextInstrAddr();
		Machine.emit(Op.HALT, 0, 0, 0);
		
		for (ClassDecl cd : prog.classDeclList) {
			setUpClassAndFieldRED(cd);
		}
		
		
		for (ClassDecl cd : prog.classDeclList) {
			cd.visit(this, arg);
		}
		
		Machine.patch(2, mainMethodAddr);
		
		return null;
	}

	public void setUpClassAndFieldRED(ClassDecl cd) {
		int numFields = cd.getNumberInstanceFields();
		cd.RED = new RuntimeEntity(numFields, -1);
		int count = 0;
		
		
		// Non-static fields
		for (FieldDecl fd: cd.fieldDeclList) {
			if (fd.isStatic) {
				continue;
			}
			fd.visit(this, null);
			fd.RED.offset = count;
			count += 1;
		}
	}
	
	@Override
	public Object visitClassDecl(ClassDecl cd, Object arg) {
		
		/*
		int numFields = cd.getNumberInstanceFields();
		cd.RED = new RuntimeEntity(numFields, -1);
		int count = 0;
		
		
		// Non-static fields
		for (FieldDecl fd: cd.fieldDeclList) {
			if (fd.isStatic) {
				continue;
			}
			fd.visit(this, null);
			fd.RED.offset = count;
			count += 1;
		}
		*/
		/*
		// static fields
		for (FieldDecl fd: cd.fieldDeclList) {
			if (!fd.isStatic) {
				continue;
			}
			
			fd.visit(this, null);
		}
		*/
		
		for (MethodDecl md: cd.methodDeclList) {
			md.visit(this, null);
		}
		
		REDPrinter.printClassDecl(cd);
		return null;
	}

	@Override
	public Object visitFieldDecl(FieldDecl fd, Object arg) {
		fd.RED = new RuntimeEntity(1, 0);
		
		return null;
	}

	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		varDeclOffset = 3;
		currMethod = md;
		
		int addr = Machine.nextInstrAddr();
		Frame frame = new Frame(0, 3);
		if (md.isMain) {
			mainMethodAddr = addr;
		}
		/*
		 * TODO: Access parameters
		 */
		
		
		for (Statement st: md.statementList) {
			st.visit(this, frame);
		}

		return null;

	}

	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		
		return null;
	}

	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		
		//Frame frame = (Frame) arg;
		//decl.RED = new RuntimeEntity(1, frame.size);
		decl.RED = new RuntimeEntity(1, varDeclOffset);
		varDeclOffset += 1;
		return null;
	}

	
	
	/********************************************************************************************************
	 * End: Decls
	 * 
	 * Start: Types
	 *******************************************************************************************************/
	
	@Override
	public Object visitBaseType(BaseType type, Object arg) {
		
		return null;
	}

	@Override
	public Object visitClassType(ClassType type, Object arg) {
		
		return null;
	}

	@Override
	public Object visitArrayType(ArrayType type, Object arg) {
		
		return null;
	}

	@Override
	public Object visitNullType(NullType type, Object arg) {
		
		return null;
	}

	
	
	/********************************************************************************************************
	 * End: Types
	 * 
	 * Start: Statements
	 *******************************************************************************************************/
	
	@Override
	public Object visitBlockStmt(BlockStmt stmt, Object arg) {
		for (Statement st: stmt.sl) {
			st.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		Frame frame = (Frame) arg;
		stmt.initExp.visit(this, null);
		stmt.varDecl.visit(this, frame);
		return null;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		stmt.val.visit(this, null);
		Machine.emit(Op.STORE, 1, Reg.LB, stmt.ref.decl.RED.offset);
		return null;
	}

	//TODO: CallStmt
	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		Frame frame = (Frame) arg;
		try {
			QualRef a = (QualRef) stmt.methodRef;
			QualRef b = (QualRef) a.ref;
			IdRef c = (IdRef) b.ref;
			if (a.id.spelling.equals("println") && b.id.spelling.equals("out") && c.id.spelling.equals("System")){
				stmt.argList.get(0).visit(this, frame);
				Machine.emit(Prim.putintnl);
			}
		} catch (ClassCastException e) {
			
		}
		
		
		return null;
	}

	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		int d = currMethod.parameterDeclList.size();
		int n;
		if (stmt.returnExpr == null) {
			n = 0;
		} else {
			stmt.returnExpr.visit(this, null);
			n = 1;
		}
		
		Machine.emit(Op.RETURN, n, 0, d);
		//Machine.emit(Op.RETURN, 0, 0, 1);
		return null;
	}

	@Override
	public Object visitIfStmt(IfStmt stmt, Object arg) {
		if (stmt.elseStmt == null) {
			return visitShortIfStmt(stmt, arg);
		} else {
			return visitLongIfStmt(stmt, arg);
		}
	}
	
	
	//if statement
	public Object visitShortIfStmt(IfStmt stmt, Object arg) {
		stmt.cond.visit(this, null);
		
		
		int ifAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMPIF, 1, Reg.CB, 0); // patch me
		
		int elseAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, 0, Reg.CB, 0);
		
		
		int thenCondAddr = Machine.nextInstrAddr();
		stmt.thenStmt.visit(this, null);
		
		
		int elseCondAddr = Machine.nextInstrAddr();
		Machine.patch(ifAddr, thenCondAddr);
		Machine.patch(elseAddr, elseCondAddr);
		return null;
	}
	
	// If-else statement
	public Object visitLongIfStmt(IfStmt stmt, Object arg) {
		stmt.cond.visit(this, null);
		
		
		int ifJumpAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMPIF, 1, Reg.CB, 0); // patch me
		
		int elseJumpAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0);
		
		
		
		int thenCondAddr = Machine.nextInstrAddr();
		stmt.thenStmt.visit(this, null);
		int if_doneJumpAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0);
		
		
		int elseCondAddr = Machine.nextInstrAddr();
		stmt.elseStmt.visit(this, null);
		int else_doneJumpAddr = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0);
		
		int allCondAddr = Machine.nextInstrAddr();
		Machine.patch(ifJumpAddr, thenCondAddr);
		Machine.patch(elseJumpAddr, elseCondAddr);
		
		Machine.patch(if_doneJumpAddr, allCondAddr);
		Machine.patch(else_doneJumpAddr, allCondAddr);
		return null;
	}
	
	
	
	@Override
	public Object visitWhileStmt(WhileStmt stmt, Object arg) {
		int j = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0); // patch me
		int g = Machine.nextInstrAddr();
		stmt.body.visit(this, null);
		int h = Machine.nextInstrAddr();
		Machine.patch(j,  h);
		stmt.cond.visit(this, null);
		Machine.emit(Op.JUMPIF, 1 ,Reg.CB, g);
		
		
		return null;
	}
	

	
	/********************************************************************************************************
	 * End: Statements
	 * 
	 * Start: Expressions
	 *******************************************************************************************************/
	
	@Override
	public Object visitUnaryExpr(UnaryExpr expr, Object arg) {
		expr.expr.visit(this, null);
		expr.operator.visit(this, "unary");
		return null;
	}

	@Override
	public Object visitBinaryExpr(BinaryExpr expr, Object arg) {
		expr.left.visit(this, null);
		expr.right.visit(this, null);
		expr.operator.visit(this, null);
		return null;
	}

	@Override
	public Object visitRefExpr(RefExpr expr, Object arg) {
		Frame frame = (Frame) arg;
		expr.ref.visit(this, frame);
		return null;
	}

	@Override
	public Object visitCallExpr(CallExpr expr, Object arg) {
		
		return null;
	}

	@Override
	public Object visitLiteralExpr(LiteralExpr expr, Object arg) {
		expr.lit.visit(this, null);
		return null;
	}

	@Override
	public Object visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		Machine.emit(Op.LOADL, -1);
		
		return null;
	}

	@Override
	public Object visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		
		return null;
	}

	@Override
	public Object visitThisRef(ThisRef ref, Object arg) {
		
		return null;
	}

	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		System.out.println("===========================================================");
		System.out.print("Visiting identifier: " + ref.id.spelling);
		System.out.println(" with offset: " + ref.decl.RED.offset);
		System.out.println("===========================================================");
		Frame frame = (Frame) arg;
		Machine.emit(Op.LOAD, Reg.LB, ref.decl.RED.offset);
		System.out.println("IDREF");
		return null;
	}

	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		
		return null;
	}

	@Override
	public Object visitIxRef(IxRef ref, Object arg) {
		
		return null;
	}

	@Override
	public Object visitIdentifier(Identifier id, Object arg) {
		System.out.println("IDENTIFIER");
		return null;
	}

	@Override
	public Object visitOperator(Operator op, Object arg) {
		String x = op.spelling;
		
		if (arg instanceof String && arg.equals("unary")) {
			if (x.equals("!")) {
				Machine.emit(Prim.not);
			} else if (x.equals("||")) {
				Machine.emit(Prim.or);
			} else if (x.equals("&&")) {
				Machine.emit(Prim.and);
			} else if (x.equals("-")) {
				Machine.emit(Prim.neg);
			}
			return null;
		}
		
		
		
		
		if (x.equals("+")) {
			Machine.emit(Prim.add);
		} else if (x.equals("*")) {
			Machine.emit(Prim.mult);
		} else if (x.equals("-")) {
			Machine.emit(Prim.sub);
		} else if (x.equals("/")) {
			Machine.emit(Prim.div);
		} else if (x.equals("%")) {
			Machine.emit(Prim.mod);
		} else if (x.equals("<")) {
			Machine.emit(Prim.lt);
		} else if (x.equals("<=")) {
			Machine.emit(Prim.le);
		} else if (x.equals(">")) {
			Machine.emit(Prim.gt);
		} else if (x.equals(">=")) {
			Machine.emit(Prim.ge);
		} else if (x.equals("==")) {
			Machine.emit(Prim.eq);
		} else if (x.equals("!=")) {
			Machine.emit(Prim.ne);
		}
		return null;
	}

	@Override
	public Object visitIntLiteral(IntLiteral num, Object arg) {
		Machine.emit(Op.LOADL, Integer.parseInt(num.spelling));
		return null;
	}

	@Override
	public Object visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		String spelling = bool.spelling;
		if (spelling.equals("true")) {
			Machine.emit(Op.LOADL, 1);
		} else {
			Machine.emit(Op.LOADL, 0);
		}
		return null;
	}

	@Override
	public Object visitNullLiteral(NullLiteral nullLit, Object arg) {
		Machine.emit(Op.LOADL, 0);
		return null;
	}

}
