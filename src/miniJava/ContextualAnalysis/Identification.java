package miniJava.ContextualAnalysis;

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
import miniJava.AbstractSyntaxTrees.Expression;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxRef;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.LocalDecl;
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
import miniJava.AbstractSyntaxTrees.ReturnStmt;
import miniJava.AbstractSyntaxTrees.Statement;
import miniJava.AbstractSyntaxTrees.ThisRef;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;

public class Identification implements Visitor<Object, Object>{
	// <Argtype, result type>
	
	private IdentificationTable table;
	private ClassDeclTable classTable;
	@SuppressWarnings("unused")
	private ErrorReporter reporter;
	
	private ClassDecl currentClass;
	
	
	//private int idCalled;
	
	private boolean inStaticMethod;
	
	public Identification(AST ast, ErrorReporter reporter) {
		this.table = new IdentificationTable(reporter);
		this.classTable = new ClassDeclTable();
		this.reporter = reporter;
		//idCalled = 0;
		inStaticMethod = false;
		loadPredefined();
		ast.visit(this,  null);
		//System.out.println(idCalled);
	}
	
	private void loadPredefined() {
		for (String name: table.getPredefinedDecl().keySet()) {
			classTable.addPredefinedClassTable(name, new ClassMemberTable((ClassDecl) table.getPredefinedDecl().get(name)));
		}
	}

	// DONE
	@Override
	public Object visitPackage(Package prog, Object arg) {
		table.openScope(); // Opening scope level 1
		
		// Iterate through classes, add classDeclarations to table, and members to classTable
		for (ClassDecl classDecl: prog.classDeclList) {
			table.put(classDecl.name, classDecl);
			classTable.addClassTable(classDecl.name, new ClassMemberTable(classDecl));
		}
		
		//Visit all classes
		for (ClassDecl classDecl: prog.classDeclList) {
			currentClass = classDecl;
			classDecl.visit(this, null);
		}
		
		table.closeScope(); // Closing scope level 1
		return null;
	}

	// DONE
	@Override
	public Object visitClassDecl(ClassDecl cd, Object arg) {
		////System.out.println("PROCESSING CLASS: " + cd.name);
		
		table.openScope(); // Opening scope level 2
		
		for (FieldDecl fieldDecl: cd.fieldDeclList) {
			table.put(fieldDecl.name, fieldDecl);
		}
		for (MethodDecl methDecl: cd.methodDeclList) {
			table.put(methDecl.name, methDecl);
		}
		
		
		for (FieldDecl fieldDecl: cd.fieldDeclList) {
			fieldDecl.visit(this, null);
		}
		for (MethodDecl methDecl: cd.methodDeclList) {
			methDecl.visit(this, null);
		}
	
		table.closeScope(); // Closing scope level 2
		return null;
	}

	@Override
	public Object visitFieldDecl(FieldDecl fd, Object arg) {
		// Do I need to edit this? Field Decls are added in visitPackage
		
		// Visiting the type of the field // Do I need this?
		fd.type.visit(this, null); // Calls TypeDenoter.visit
		// if the field is a class, itll call visitClassType()
		
		//table.put(fd.name, fd);
		return null;
	}

	// DONE
	@Override
	public Object visitMethodDecl(MethodDecl md, Object arg) {
		////System.out.println("PROCESSING METHOD: " + md.name);
		
		//table.put(md.name, md);
		if (md.isStatic) {
			inStaticMethod = true;
			table.onlyStatic();
		}
		md.type.visit(this, null);
		table.openScope();
		for (ParameterDecl pd: md.parameterDeclList) {
			pd.visit(this, null);
		}
		table.openScope();
		for (Statement stmt: md.statementList) {
			stmt.visit(this, null);
		}
		table.closeScope();
		table.closeScope();
		//inStaticMethod = false;
		
		inStaticMethod = false;
		table.allAccess();
		return null;
	}

	// DONE
	@Override
	public Object visitParameterDecl(ParameterDecl pd, Object arg) {
		table.put(pd.name, pd);
		
		// Do I need this?
		pd.type.visit(this, null);
		return null;
	}

	// Done
	@Override
	public Object visitVarDecl(VarDecl decl, Object arg) {
		////System.out.println("in vardecl");
		table.put(decl.name, decl);
		decl.type.visit(this, null);
		
		return null;
	}

	@Override
	public Object visitBaseType(BaseType type, Object arg) {
		// Leave blank cause we dont care about types?
		////System.out.println("VISITING BASE TYPE");
		return null;
	}

	/*
	  @see miniJava.AbstractSyntaxTrees.Visitor#visitClassType(miniJava.AbstractSyntaxTrees.ClassType, java.lang.Object)
	 */
	@Override
	public Object visitClassType(ClassType type, Object arg) {
		// CONTEXT: Need to find referenced class
		// Calls visit identifier
		type.className.visit(this, "class");
		return null;
	}

	@Override
	public Object visitArrayType(ArrayType type, Object arg) {
		////System.out.println("VISIT ARRAY TYPE");
		type.eltType.visit(this, null);
		return null;
	}

	// DONE
	@Override
	public Object visitBlockStmt(BlockStmt stmt, Object arg) {
		table.openScope();
		for (Statement st: stmt.sl) {
			st.visit(this, null);
		}
		table.closeScope();
		return null;
	}

	// HERE
	@Override
	public Object visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		//stmt.initExp
		// This will result in the variables name being put into the table
		
		stmt.varDecl.visit(this, null);
		table.disallowAccess(stmt.varDecl.name);
		if (stmt.initExp instanceof NewArrayExpr) {
			System.out.println("Oh boy its a new array expression");
		}
		stmt.initExp.visit(this, null);
		table.allowAccess();
		
		return null;
	}

	// HERE
	@Override
	public Object visitAssignStmt(AssignStmt stmt, Object arg) {
		
		if (stmt.val instanceof NewArrayExpr) {
			System.out.println("Oh girl its a new array expression");
		}
		stmt.val.visit(this, null);
		
		stmt.ref.visit(this, null);
		
		//stmt.val.visit(this, null);
		
		return null;
	}

	/*
	 * Very difficult to deal with
	 */
	@Override
	public Object visitCallStmt(CallStmt stmt, Object arg) {
		// calls reference.visit
		stmt.methodRef.visit(this, null);
		for (Expression expr: stmt.argList) {
			expr.visit(this, null);
		}
		return null;
	}

	// DONE
	@Override
	public Object visitReturnStmt(ReturnStmt stmt, Object arg) {
		////System.out.println("VISITING RETURN STATEMENT");
		if (stmt.returnExpr != null) {
			stmt.returnExpr.visit(this, null);
		}
		return null;
	}

	// DONE
	@Override
	public Object visitIfStmt(IfStmt stmt, Object arg) {
		
		stmt.cond.visit(this, null);
		
		if (stmt.thenStmt instanceof VarDeclStmt) {
			report(stmt.thenStmt.posn.start, "Identification", "Variable declaration not allowed here");
			System.exit(4);
		}
		
		table.openScope();
		stmt.thenStmt.visit(this, null);
		table.closeScope();
		
		if (stmt.elseStmt != null) {
			if (stmt.elseStmt instanceof VarDeclStmt) {
				report(stmt.elseStmt.posn.start, "Identification", "Variable declaration not allowed here");
				System.exit(4);
			}
			table.openScope();
			stmt.elseStmt.visit(this, null);
			table.closeScope();
		}
		
		return null;
		
	}

	// DONE
	@Override
	public Object visitWhileStmt(WhileStmt stmt, Object arg) {
		
		
		stmt.cond.visit(this, null);
		
		if (stmt.body instanceof VarDeclStmt) {
			report(stmt.body.posn.start, "Identification", "Variable declaration not allowed here");
			System.exit(4);
		}
		
		table.openScope();
		stmt.body.visit(this, null);
		table.closeScope();
		
		return null;
	}

	// DONE
	@Override
	public Object visitUnaryExpr(UnaryExpr expr, Object arg) {
		expr.expr.visit(this, null);
		return null;
	}

	// DONE
	@Override
	public Object visitBinaryExpr(BinaryExpr expr, Object arg) {
		expr.left.visit(this, null);
		expr.right.visit(this, null);
		
		return null;
	}

	// DONE (I THINK)
	@Override
	public Object visitRefExpr(RefExpr expr, Object arg) {
		expr.ref.visit(this, null);
		return null;
	}

	@Override
	public Object visitCallExpr(CallExpr expr, Object arg) {
		// CallExpr is composed of the reference (functionRef), and the argument list (argList)
		// Need to check if the reference is valid, and the visit the arglist

		// Is this enough?
		expr.functionRef.visit(this, null);
		
		
		for (Expression argExpr: expr.argList) {
			argExpr.visit(this, null);
		}
		return null;
	}

	// DONE
	@Override
	public Object visitLiteralExpr(LiteralExpr expr, Object arg) {
		// Leave blank?
		return null;
	}

	@Override
	public Object visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		expr.classtype.visit(this, null);
		
		return null;
	}

	@Override
	public Object visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		
		expr.eltType.visit(this, null);
		expr.sizeExpr.visit(this, null);
		
		return null;
	}


	// DONE
	@Override
	public Object visitOperator(Operator op, Object arg) {
		// Leave blank
		return null;
	}

	// DONE
	@Override
	public Object visitIntLiteral(IntLiteral num, Object arg) {
		// Leave blank since we aren't concerned with types
		return null;
	}
	// DONE
	@Override
	public Object visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		// Leave blank since we aren't concerned with types
		return null;
	}

	// Always part of a qualRef
	@Override
	public Object visitThisRef(ThisRef ref, Object arg) {
		// CONTEXT: Need to find member within current class
		
		if (inStaticMethod) {
			report(ref.posn.start, "Identification", "Cannot reference this in a static reference");
		}
		ref.decl = currentClass;
		return null;
	}
	// Can be part of a qualRef, or can stand alone
	@Override
	public Object visitIdRef(IdRef ref, Object arg) {
		// CONTEXT: Need to find closest variable declaration with this name
		Declaration temp = (Declaration) ref.id.visit(this, "idRef");
		ref.decl = temp;
		return temp;
	}

	// ref.ref can be ThisRef, IdREf, QRef
	@Override
	public Object visitQRef(QualRef ref, Object arg) {
		//System.out.println("QualRef called");
		// CONTEXT: Need to find associated field (ref.id) within ref.ref
		// Need to determine the type of ref.ref
		// If this: just check the current class' fields
		// If id: check fields of the reference
		// if qualRef: check the fields of the ref.ref.id.decl
		
		Declaration decl = null;
		
		if (ref.ref instanceof IdRef) {
			// Call visitIdRef
			decl = (Declaration) ref.ref.visit(this, null);
			if (decl instanceof ClassDecl) {
				ClassDecl tempDecl = (ClassDecl) decl;
				// Static Reference
				decl = (Declaration) ref.id.visit(this, "~:::" + tempDecl.name);
			} else if (decl instanceof LocalDecl) {
				// Non Static Reference
				LocalDecl tempDecl = (LocalDecl) decl;
				ClassType cType = null;
				ArrayType aType = null;
				try {
					cType = (ClassType) tempDecl.type;
					decl = (Declaration) ref.id.visit(this, cType.className.spelling);
				} catch (ClassCastException e) {
					report(tempDecl.posn.start, "Identification", "Attempting to dereference a non-object type");
					System.exit(4);
				}
//				decl = (Declaration) ref.id.visit(this, cType.className.spelling);
			} else if (decl instanceof MemberDecl) {
				//Non static reference
				MemberDecl tempDecl = (MemberDecl) decl;
				ClassType cType = null;
				ArrayType aType = null;
				try {
					cType = (ClassType) tempDecl.type;
					decl = (Declaration) ref.id.visit(this, cType.className.spelling);
				} catch(ClassCastException e) {
					report(tempDecl.posn.start, "Identification", "Attempting to dereference a non-object type");
					System.exit(4);
				}
//				decl = (Declaration) ref.id.visit(this, cType.className.spelling);
			}
		} else if (ref.ref instanceof ThisRef) {
			// Call visitThisRef
			ref.ref.visit(this, null);
			decl = (Declaration) ref.id.visit(this, "thisRef");
		} else if (ref.ref instanceof QualRef) {
			// Call visitQRef
			decl = (Declaration) ref.ref.visit(this, null);
			if (decl instanceof ClassDecl) {
				ClassDecl tempDecl = (ClassDecl) decl;
				// Static Reference
				decl = (Declaration) ref.id.visit(this, "~:::" + tempDecl.name);
			} else if (decl instanceof LocalDecl) {
				// Non Static Reference
				LocalDecl tempDecl = (LocalDecl) decl;
				ClassType cType = null;
				ArrayType aType = null;
				try {
					cType = (ClassType) tempDecl.type;
					decl = (Declaration) ref.id.visit(this, cType.className.spelling);
				} catch (ClassCastException e) {
					report(tempDecl.posn.start, "Identification", "Attempting to dereference a non-object type");
					System.exit(4);
				}
//				decl = (Declaration) ref.id.visit(this, cType.className.spelling);
				
			} else if (decl instanceof MemberDecl) {
				//Non static reference
				MemberDecl tempDecl = (MemberDecl) decl;
				ClassType cType = null;
				ArrayType aType = null;
				try {
					cType = (ClassType) tempDecl.type;
					decl = (Declaration) ref.id.visit(this, cType.className.spelling);
				} catch(ClassCastException e) {
					report(tempDecl.posn.start, "Identification", "Attempting to dereference a non-object type");
					System.exit(4);
				}
//				decl = (Declaration) ref.id.visit(this, cType.className.spelling);
			} else {
				//System.out.println("IN THIS ELSE BLOCK +++++++++++++++++++++++++++");
				//System.out.println(decl);
			}
		}
		ref.decl = decl;
		
		return decl;
	}

	// Never a part of a qualref, but can contain a qualref
	@Override
	public Object visitIxRef(IxRef ref, Object arg) {
		// TODO Auto-generated method stub
		ref.indexExpr.visit(this, null);
		Declaration temp = (Declaration) ref.ref.visit(this, null);
		ref.decl = temp;
		return null;
	}

	/*
	 * id is the name of the field
	 * arg is a string representing the context/class
	 */
	@Override
	public Declaration visitIdentifier(Identifier id, Object arg) {
		//idCalled ++;
		String errorCause = "";
		String context = (String) arg;
		
		//System.out.println("CONTEXT: " + context);
		Declaration decl = null;
		if (context == "thisRef") {
			// Member context
			decl = table.get(id.spelling, 2,  id.posn.start);
			if (decl == null) {
				errorCause = "Non existant class member (" + currentClass.name + "." + id.spelling + ")"; 
			}
		} else if (context == "idRef") {
			System.out.println("IN ID REF: " + id.spelling);
			//Local context
			decl = table.get(id.spelling, id.posn.start);
			//System.out.println("DECL: " + decl);
			if (decl == null) {
				errorCause = "No visible variable named (" + id.spelling + ")";
			}
		} else if (context.charAt(0) == '~'){
			//Static context
			String currentName = currentClass.name;
			String className = context.substring(4);
			if (currentName.equals(className)) {
				decl = classTable.getClassTable(className).getStatic(id.spelling);
			} else {
				decl = classTable.getClassTable(className).getPublicStatic(id.spelling);
			}
			
			if (decl == null) {
				errorCause = "Static reference of a non static member (" + id.spelling + ")";
			}
		} else if (context == "class") {
			decl = table.get(id.spelling, 0, 1, id.posn.start);
			if (decl == null) {
				errorCause = "Non existant class (" + id.spelling + ")";
			}
		} else {
			
			decl = classTable.getClassTable(context).getPublic(id.spelling);
			
			if (table.get(id.spelling, id.posn.start) == currentClass) {
				System.out.println(" IN HERE IN HERE IN HERE");
			}
			
			if (decl == null) {
				if (!classTable.getClassTable(context).contains(id.spelling)) {
					errorCause = "Non existant class member (" + context + "." + id.spelling + ")";
				} else {
					errorCause = "Innacessible class member (" + context + "." + id.spelling + ")";
				}
			}
		}
		
		if (decl == null) {
			report(id.posn.start, "Identification", errorCause);
			//reporter.reportError("*** Error Linking Identifier: " + id.spelling + " at line numbers (" + id.posn.start +" - " + id.posn.finish + ") \n Cause: " + errorCause);
			System.exit(4);
		} else {
			id.decl = decl;
		}
		return decl;
	}

	@Override
	public Object visitNullLiteral(NullLiteral nullLit, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNullType(NullType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void report(int line, String type, String reason) {
		reporter.reportError("*** line " + line + ":  " + type + " error - " + reason);
	}

	
	

}
