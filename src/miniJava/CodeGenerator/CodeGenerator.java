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
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.Expression;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxRef;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.MemberDecl;
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
import miniJava.AbstractSyntaxTrees.Reference;
import miniJava.AbstractSyntaxTrees.ReturnStmt;
import miniJava.AbstractSyntaxTrees.Statement;
import miniJava.AbstractSyntaxTrees.ThisRef;
import miniJava.AbstractSyntaxTrees.TypeDenoter;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;

import mJAM.Machine;
import mJAM.Machine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**************************************************
 * TODO:
 * 	Alter method calls to allow for static or non-static methods
 *  Deal with nested qualrefs fieldupds not being saved
 * 
 *
 **************************************************/

public class CodeGenerator implements Visitor<Object, Object>{
	/*
	 * Need to visit all decls and generate runtime entities for them
	 * 
	 * Decls dont deal with frames?
	 */
	
	public int mainMethodAddr = 0;
	
	public int varDeclOffset = 0;
	public int parameterDeclOffset = 0;
	public int staticBaseOffset = 0;
	public MethodDecl currMethod;
	
	public int popSize = 0;
	
	
	public boolean assigning;
	
	public Map<MethodDecl, List<Integer>> backPatchMap;
	
	private void addToMap(MethodDecl md, int addr) {
		if (backPatchMap.containsKey(md)) {
			backPatchMap.get(md).add(new Integer(addr));
		} else {
			backPatchMap.put(md, new ArrayList<Integer>());
			backPatchMap.get(md).add(new Integer(addr));
		}
	}
	
	private void backPatch() {
		List<MethodDecl> keys = new ArrayList<>(backPatchMap.keySet());
		
		for (MethodDecl md: keys) {
			for (Integer offset: backPatchMap.get(md)) {
				Machine.patch(offset.intValue(), md.RED.offset);
			}
		}
	}
	
	
	public void encodeRun(AST ast) {
		ast.visit(this, null);
	}
	
	@Override
	public Object visitPackage(Package prog, Object arg) {
		
		backPatchMap = new HashMap<MethodDecl, List<Integer>>();
		
		/* Pre-preamble*/
		for (ClassDecl cd: prog.classDeclList) {
			for (FieldDecl fd: cd.fieldDeclList) {
				if (fd.isStatic) {
					fd.visit(this, null);
					fd.RED.offset = staticBaseOffset;
					staticBaseOffset += 1;
					Machine.emit(Op.PUSH, Reg.SB, 1);
					Machine.emit(Op.LOADL, 0);
					Machine.emit(Op.STORE, 0, Reg.SB, fd.RED.offset);
				}
			}
		}
		
		
		
		
		
		/* Preamble */
		Machine.emit(Op.LOADL, 0);
		Machine.emit(Prim.newarr);
		int tempAddr = Machine.nextInstrAddr();
		Machine.emit(Op.CALL, 0, Reg.CB, 1);
		Machine.emit(Op.HALT, 0, 0, 0);
		
		for (ClassDecl cd : prog.classDeclList) {
			setUpClassAndFieldRED(cd);
		}
		
		
		for (ClassDecl cd : prog.classDeclList) {
			cd.visit(this, arg);
		}
		
		Machine.patch(tempAddr, mainMethodAddr);
		backPatch();
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
		
		
		// static fields
//		for (FieldDecl fd: cd.fieldDeclList) {
//			if (fd.isStatic) {
//				fd.visit(this, null);
//				fd.RED.offset = staticBaseOffset;
//				staticBaseOffset += 1;
//			}
//		}
		
		
		for (MethodDecl md: cd.methodDeclList) {
			md.visit(this, null);
		}
		
		//REDPrinter.printClassDecl(cd);
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
		parameterDeclOffset = -1;
		currMethod = md;
		
		int startAddr = Machine.nextInstrAddr();
		Frame frame = new Frame(0, 3);
		if (md.isMain) {
			mainMethodAddr = startAddr;
		}
		
		for (ParameterDecl pd: md.parameterDeclList) {
			pd.visit(this, null);
		}
		
		for (Statement st: md.statementList) {
			st.visit(this, null);
		}
		
		int endAddr = Machine.nextInstrAddr();
		md.RED = new RuntimeEntity(endAddr - startAddr, startAddr);

		return null;

	}

	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		pd.RED = new RuntimeEntity(1, parameterDeclOffset);
		parameterDeclOffset -=1;
		return null;
	}

	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		popSize += 1;
		decl.RED = new RuntimeEntity(1, varDeclOffset);
		varDeclOffset += 1;
		
		//Machine.emit(Op.PUSH, 1);
		//Machine.emit(Op.STORE, Reg.LB, decl.RED.offset);
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
		Integer popSize = new Integer(0);
		
		for (Statement st: stmt.sl) {
			Object temp = st.visit(this, popSize);
			if (temp instanceof Integer) {
				popSize = (Integer) temp;
			}
		}
		Machine.emit(Op.POP, popSize.intValue());
		varDeclOffset -= popSize.intValue();
		//Machine.emit(Op.POP, popSize);
		return null;
	}

	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		// To keep track of popping of stack
		if (arg instanceof Integer) {
			arg = new Integer(((Integer) arg).intValue() + 1);
		}
		stmt.initExp.visit(this, null);
		stmt.varDecl.visit(this, arg);
		//Machine.emit(Op.STORE, Reg.SB, stmt.varDecl.RED.offset);
		return arg;
	}

	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		encodeStore(stmt.ref, stmt.val);
	
		return null;
	}

	//TODO: CallStmt
	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		try {
			QualRef a = (QualRef) stmt.methodRef;
			QualRef b = (QualRef) a.ref;
			IdRef c = (IdRef) b.ref;
			if (a.id.spelling.equals("println") && b.id.spelling.equals("out") && c.id.spelling.equals("System")){
				stmt.argList.get(0).visit(this, null);
				Machine.emit(Prim.putintnl);
				return null;
			}
			
		} catch (ClassCastException e) {
			
		}
		
		MethodDecl md = (MethodDecl) stmt.methodRef.decl;
		if (stmt.methodRef.decl.RED != null) {
			// Have already visited this decl
			for (int x = stmt.argList.size() - 1; x >= 0; x--) {
				stmt.argList.get(x).visit(this, null);
			}
			if (md.isStatic) {
				Machine.emit(Op.CALL, stmt.methodRef.decl.RED.offset);
			} else {
				
				
				if (stmt.methodRef instanceof QualRef) {
					QualRef temp = (QualRef) stmt.methodRef;
					encodeFetch(temp.ref);
				} else {
					Machine.emit(Op.LOAD, Reg.OB, 0);
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				Machine.emit(Op.CALLI, Reg.CB, stmt.methodRef.decl.RED.offset);
			}
		} else {
			for (int x = stmt.argList.size() - 1; x >= 0; x--) {
				stmt.argList.get(x).visit(this, null);
			}
			if (md.isStatic) {
				int addr2 = Machine.nextInstrAddr();
				Machine.emit(Op.CALL, Reg.CB, 0);
				addToMap((MethodDecl)stmt.methodRef.decl, addr2);
			} else {
				if (stmt.methodRef instanceof QualRef) {
					QualRef temp = (QualRef) stmt.methodRef;
					encodeFetch(temp.ref);
				} else {
					Machine.emit(Op.LOAD, Reg.OB, 0);
				}
				
				int addr2 = Machine.nextInstrAddr();
				Machine.emit(Op.CALLI, Reg.CB, 0);
				addToMap((MethodDecl)stmt.methodRef.decl, addr2);
			}
			
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
		if (expr.operator.spelling.equals("&&")) {
			expr.left.visit(this, null);
			int addr1 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMPIF, 0, Reg.CB, 0);
			Machine.emit(Op.LOADL, 1);
			
			
			expr.right.visit(this, null);
			expr.operator.visit(this, null); // will put 1 or 0 on the stack top
			//Machine.emit(Prim.putintnl);
			int addr3 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, 0, Reg.CB, 0);
			
			
			int shortCircuitAddress = Machine.nextInstrAddr();
			Machine.emit(Op.LOADL, 0);
			int addr2 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, Reg.CB, 0);
			
			
			
			int endAddr = Machine.nextInstrAddr();
			
			
			Machine.patch(addr1, shortCircuitAddress);
			Machine.patch(addr2, endAddr);
			Machine.patch(addr3, endAddr);
		} else if (expr.operator.spelling.equals("||")) {
			// logic expression, might need to short circuit
			expr.left.visit(this, null);
			int addr1 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMPIF, 1, Reg.CB, 0);
			Machine.emit(Op.LOADL, 0);
			
			
			expr.right.visit(this, null);
			expr.operator.visit(this, null);
			int addr3 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, 0, Reg.CB, 0);
			
			
			int shortCircuitAddress = Machine.nextInstrAddr();
			Machine.emit(Op.LOADL, 1);
			int addr2 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, Reg.CB, 0);
			
			
			
			
			expr.right.visit(this, null);
			expr.operator.visit(this, null);
			
			
			
			
			int endAddr = Machine.nextInstrAddr();
			Machine.patch(addr1, shortCircuitAddress);
			Machine.patch(addr2, endAddr);
			Machine.patch(addr3, endAddr);
		} else {
			expr.left.visit(this, null);
			expr.right.visit(this, null);
			expr.operator.visit(this, null);
		}
		return null;
	}

	
	// TODO: Issues with references
	@Override
	public Object visitRefExpr(RefExpr expr, Object arg) {
		
		if (expr.ref instanceof QualRef) {
			QualRef temp = (QualRef) expr.ref;
			Declaration decl = temp.ref.decl;
			TypeDenoter type = decl.type;
			if (type instanceof ArrayType) {
				encodeFetch(temp.ref);
				Machine.emit(Prim.arraylen);
				return null;
				
			}
		}
		
		
		encodeFetch(expr.ref);
		//Machine.emit(Op.LOADL, expr.ref.decl.RED.offset);
		
		//Machine.emit(Prim.fieldref);
		return null;
	}

	@Override
	public Object visitCallExpr(CallExpr expr, Object arg) {
		/*
		if (expr.functionRef.decl.RED != null) {
			// Have already visited this decl
			for (int x = expr.argList.size() - 1; x >= 0; x--) {
				expr.argList.get(x).visit(this, null);
			}
			Machine.emit(Op.CALL, expr.functionRef.decl.RED.offset);
		} else {
			for (int x = expr.argList.size() - 1; x >= 0; x--) {
				expr.argList.get(x).visit(this, null);
			}
			int addr2 = Machine.nextInstrAddr();
			Machine.emit(Op.CALL, Reg.CB, 0);
			addToMap((MethodDecl)expr.functionRef.decl, addr2);
			
		}
		return null;
		*/
		MethodDecl md = (MethodDecl) expr.functionRef.decl;
		if (expr.functionRef.decl.RED != null) {
			// Have already visited this decl
			for (int x = expr.argList.size() - 1; x >= 0; x--) {
				expr.argList.get(x).visit(this, null);
			}
			if (md.isStatic) {
				Machine.emit(Op.CALL, Reg.CB, expr.functionRef.decl.RED.offset);
			} else {
				
				
				if (expr.functionRef instanceof QualRef) {
					QualRef temp = (QualRef) expr.functionRef;
					encodeFetch(temp.ref);
				} else {
					Machine.emit(Op.LOADA, Reg.OB, 0);
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				Machine.emit(Op.CALLI, Reg.CB, expr.functionRef.decl.RED.offset);
			}
		} else {
			for (int x = expr.argList.size() - 1; x >= 0; x--) {
				expr.argList.get(x).visit(this, null);
			}
			if (md.isStatic) {
				int addr2 = Machine.nextInstrAddr();
				Machine.emit(Op.CALL, Reg.CB, 0);
				addToMap((MethodDecl)expr.functionRef.decl, addr2);
			} else {
				if (expr.functionRef instanceof QualRef) {
					QualRef temp = (QualRef) expr.functionRef;
					encodeFetch(temp.ref);
				} else {
					Machine.emit(Op.LOADA, Reg.OB, 0);
				}
				
				int addr2 = Machine.nextInstrAddr();
				Machine.emit(Op.CALLI, Reg.CB, 0);
				addToMap((MethodDecl)expr.functionRef.decl, addr2);
			}
			
		}

		
		return null;
	}

	@Override
	public Object visitLiteralExpr(LiteralExpr expr, Object arg) {
		expr.lit.visit(this, null);
		return null;
	}

	@Override
	public Object visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		Declaration temp = expr.classtype.className.decl;
		Machine.emit(Op.LOADL, -1);
		Machine.emit(Op.LOADL, temp.RED.size);
		Machine.emit(Prim.newobj);
		return null;
	}

	@Override
	public Object visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		expr.sizeExpr.visit(this, null);
		Machine.emit(Prim.newarr);
		return null;
	}

	/********************************************************************************************************
	 * End: Expressions
	 * 
	 * Start: References
	 *******************************************************************************************************/
	
	/*
	 * GOAL: Load address onto the stack
	 */
	// TODO: Issues with references
	@Override
	public Object visitThisRef(ThisRef ref, Object arg) {
		Machine.emit(Op.LOADA, Reg.OB, 0);
		encodeFetch(ref);
		return null;
	}
	// TODO: Issues with references
	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		
		/* NEW START */
		if (ref.decl instanceof MemberDecl) {	
			Machine.emit(Op.LOAD, Reg.OB, ref.decl.RED.offset);
		} else {
		/* NEW END */
		// Just commented out
			Machine.emit(Op.LOAD, Reg.LB, ref.decl.RED.offset);
		}
		return null;
	}

	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		
		if (ref.ref instanceof ThisRef) {
			Machine.emit(Op.LOADA, Reg.OB, 0);
			Machine.emit(Op.LOADL, ref.id.decl.RED.offset);
			return null;
		} else if (ref.ref.decl instanceof ClassDecl) {
			// static references
			Machine.emit(Op.LOADA, Reg.SB, 0/*ref.id.decl.RED.offset*/);
			Machine.emit(Op.LOADL, ref.id.decl.RED.offset);
			return null;
		}
		
		
		
		
		ref.ref.visit(this, null);
		if (ref.ref instanceof QualRef) {
			Machine.emit(Prim.fieldref);
		}
		Machine.emit(Op.LOADL, Reg.LB, ref.decl.RED.offset);
		return null;
	}


	@Override
	public Object visitIxRef(IxRef ref, Object arg) {
		encodeFetch(ref.ref);
		ref.indexExpr.visit(this, null);
		return null;
	}

	/********************************************************************************************************
	 * End: References
	 * 
	 * Start: Misc.
	 *******************************************************************************************************/
	
	
	@Override
	public Object visitIdentifier(Identifier id, Object arg) {
		Machine.emit(Op.LOADL, id.decl.RED.offset);
		return null;
	}

	@Override
	public Object visitOperator(Operator op, Object arg) {
		String x = op.spelling;
		
		if (arg instanceof String && arg.equals("unary")) {
			if (x.equals("!")) {
				Machine.emit(Prim.not);
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
		}  else if (x.equals("||")) {
			Machine.emit(Prim.or);
		} else if (x.equals("&&")) {
			Machine.emit(Prim.and);
		} 
		return null;
	}

	
	/********************************************************************************************************
	 * End: Misc.
	 * 
	 * Start: Literals
	 *******************************************************************************************************/
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
	
	/********************************************************************************************************
	 * End: Literals
	 * 
	 * Start: Encode Store/Fetch
	 *******************************************************************************************************/

	

	
	
	/*
	 * Working
	 */
	public void encodeStore(Reference ref, Expression exp) {
		
		Declaration decl = ref.decl;
		if (decl instanceof VarDecl || decl instanceof ParameterDecl) {
			// LocalDecl (DONE)
			if (ref instanceof IxRef) {ref.visit(this, null);
				exp.visit(this, null);
				Machine.emit(Prim.arrayupd);
			} else {
				exp.visit(this, null);
				Machine.emit(Op.STORE, 0, Reg.LB, decl.RED.offset);
			}
		} else {
			// Member decl
			
			
			if (ref instanceof QualRef) {
				QualRef temp = (QualRef) ref;
				if (temp.ref.decl instanceof ClassDecl && !(temp.ref instanceof ThisRef)) {
					exp.visit(this, null);
					Machine.emit(Op.STORE, 0, Reg.SB, temp.id.decl.RED.offset);
				} else if (temp.ref instanceof ThisRef) {
					//ref.visit(this, null);
					exp.visit(this, null);
					Machine.emit(Op.STORE, 0, Reg.OB, temp.id.decl.RED.offset);
				} else {
					ref.visit(this, null);
					exp.visit(this, null);
				
					Machine.emit(Prim.fieldupd);
				}
			} else if (ref instanceof IdRef) {
				// Only here for instance fields
				IdRef temp = (IdRef) ref;
				MemberDecl md = (MemberDecl) temp.decl;
				exp.visit(this, null);
				if (md.isStatic) {
					Machine.emit(Op.STORE, 0, Reg.SB, temp.id.decl.RED.offset);
				} else {
					Machine.emit(Op.STORE, 0, Reg.OB, temp.id.decl.RED.offset);
				}
			} else if (ref instanceof ThisRef) {
				//ThisRef temp = (ThisRef) ref;
				Machine.emit(Op.LOADA, Reg.OB, 0);
				// do nothing
			} else if (ref instanceof IxRef) {
				ref.visit(this, null);
				exp.visit(this, null);
				Machine.emit(Prim.arrayupd);
			}
		}
		
	}
	
	
	/*
	 * Working
	 */
	public void encodeFetch(Reference ref) {
		Declaration decl = ref.decl;
		if (decl instanceof VarDecl || decl instanceof ParameterDecl) {
			// LocalDecl (DONE)
			
			if (ref instanceof IxRef) {
				ref.visit(this, null);
				Machine.emit(Prim.arrayref);
			} else {
				Machine.emit(Op.LOAD, Reg.LB, ref.decl.RED.offset);
			}
		} else {
			if (ref instanceof QualRef) {
				QualRef temp = (QualRef) ref;
				// The temp.ref instanceof thing is weird
				if (temp.ref.decl instanceof ClassDecl && !(temp.ref instanceof ThisRef)) {
					Machine.emit(Op.LOAD, 0, Reg.SB, temp.id.decl.RED.offset);
					return;
				}
				
				ref.visit(this, null);

				
				
				if (temp.ref instanceof ThisRef) {
//					Machine.emit(Op.LOAD, Reg.OB, temp.id.decl.RED.offset);
					Machine.emit(Prim.fieldref);
				} else {
					Machine.emit(Prim.fieldref);
				}
			} else if (ref instanceof IdRef) {
				IdRef temp = (IdRef) ref;
				MemberDecl md = (MemberDecl) temp.decl;
				
				if (md.isStatic) {
					Machine.emit(Op.LOAD, 0, Reg.SB, temp.id.decl.RED.offset);
				} else {
					Machine.emit(Op.LOAD, 0, Reg.OB, temp.id.decl.RED.offset);
				}
			} else if (ref instanceof ThisRef) {
				ThisRef temp = (ThisRef) ref;
				Machine.emit(Op.LOADA, Reg.OB, 0);
				// do nothing
			} else if (ref instanceof IxRef) {
				ref.visit(this, null);
				Machine.emit(Prim.arrayref);
			}
			
		}
	}
	
	
	
	
}
