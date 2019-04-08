package miniJava.ContextualAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.BaseType;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.ClassType;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.FieldDeclList;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.MemberDecl;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.MethodDeclList;
import miniJava.AbstractSyntaxTrees.ParameterDecl;
import miniJava.AbstractSyntaxTrees.ParameterDeclList;
import miniJava.AbstractSyntaxTrees.StatementList;
import miniJava.AbstractSyntaxTrees.TypeKind;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenKind;

public class IdentificationTable {

	private List<HashMap<String, Declaration>> table;
	private int scopeLevel;
	private SourcePosition predefined;
	
	private ErrorReporter reporter;
	
	private String currentInvalidId;
	private boolean onlyStaticAccess;
	
	public IdentificationTable(ErrorReporter errorReporter) {
		this.reporter = errorReporter;
		currentInvalidId = "";
		onlyStaticAccess = false;
		table = new ArrayList<HashMap<String, Declaration>>();
		scopeLevel = 0;
		table.add(scopeLevel, new HashMap<String, Declaration>());
		predefined = new SourcePosition(0, 0);
		loadPredefinedNames();
	}
	
	public void onlyStatic() {
		onlyStaticAccess = true;
	}
	
	public void allAccess() {
		onlyStaticAccess = false;
	}
	
	public void disallowAccess(String id) {
		currentInvalidId = id;
	}
	
	public void allowAccess() {
		currentInvalidId = "";
	}
	
	public void openScope() {
		scopeLevel += 1;
		table.add(scopeLevel, new HashMap<String, Declaration>());
	}
	
	public void closeScope() {
		if (scopeLevel != 0) {
			table.remove(scopeLevel);
			scopeLevel -=1;
		}
	}
	
	public void clear() {
		for (int i=scopeLevel; i>0; i--) {
			table.remove(i);
		}
		scopeLevel = 0;
	}
	
	public boolean containsKey(String id) {
		for (int i=scopeLevel; i>=0; i--) {
			if (table.get(i).containsKey(id)) {
				return true;
			}
		}
		return false;
	}
	
	public Declaration put(String id, Declaration decl) {
		//System.out.println("PUTTING ID: " + id + " IN THE TABLE");
		if (table.get(scopeLevel).get(id) != null) {
			// Throw error (redefined variable at same scope)
			report(decl.posn.start, "Identification", "Duplicate variable declaration of (" + id + ")");
			System.exit(4);
		}
		
		if (scopeLevel >= 4) {
			if (get(id, 3, scopeLevel, -1) != null) {
				// Throw error (level 4 or higher hiding level 3 or higher)
				report(decl.posn.start, "Identification", "Declaration of (" + id + ") at level (" + scopeLevel + ") cannot override a variable at lower level");
				System.exit(4);
			}
		}
		table.get(scopeLevel).put(id, decl);
		return decl;
	}
	
	public void remove(String id) {
		if (table.get(scopeLevel).containsKey(id)) {
			table.get(scopeLevel).remove(id);
		}
	}
	
	public Declaration get(String id, int lineNumber) {
		if (id.equals(currentInvalidId)) {
			return null;
		}
		for (int i=scopeLevel; i>=0; i--) {
			if (table.get(i).containsKey(id)) {
				Declaration decl = table.get(i).get(id);
				
				if (decl instanceof MemberDecl) {
					if (!((MemberDecl) decl).isStatic && onlyStaticAccess && i == 2) {
//						report(decl.posn.start, "Identification", "Attempted access of a non-static member from a static context");
						System.out.println("SCOPE LEVEL: " + i);
						report(lineNumber, "Identification", "Attempted access of a non-static member from a static context1");
						System.exit(4);
						return null;
					}
				}
				return decl;
				
			}
		}
		return null;
	}
	
	public Declaration get(String id, int bottom, int top, int lineNumber) {
		if (id.equals(currentInvalidId)) {
			return null;
		}
		for (int i=top; i>=bottom; i--) {
			if (table.get(i).containsKey(id)) {
				Declaration decl = table.get(i).get(id);
				
				if (decl instanceof MemberDecl) {
					if (!((MemberDecl) decl).isStatic && onlyStaticAccess  && i == 2) {
//						report(decl.posn.start, "Identification", "Attempted access of a non-static member from a static context");
						report(lineNumber, "Identification", "Attempted access of a non-static member from a static context2");
						System.exit(4);
						return null;
					}
				}
				return decl;
			}
		}
		return null;
	}
	
	public Declaration get(String id, int scopeLevel, int lineNumber) {
		Declaration decl = table.get(scopeLevel).get(id);
		
		if (decl instanceof MemberDecl) {
			if (!((MemberDecl) decl).isStatic && onlyStaticAccess && scopeLevel == 2) {
//				report(decl.posn.start, "Identification", "Attempted access of a non-static member from a static context");
				report(lineNumber, "Identification", "Attempted access of a non-static member from a static context3");
				System.exit(4);
				return null;
			}
		}
		return decl;
	}
	
	
	private void loadPredefinedNames() {
		ClassDecl System = createSystem();
		put("System", System);
		ClassDecl _PrintStream = createPrintStream();
		put("_PrintStream", _PrintStream);
		ClassDecl String = createString();
		put("String", String);
	}
	
	private ClassDecl createSystem() {
		FieldDeclList fieldList = new FieldDeclList();
		ClassType type = new ClassType(new Identifier(new Token(TokenKind.CLASS, "_PrintStream", predefined)), predefined);
		FieldDecl out = new FieldDecl(false, true, type, "out", predefined);
		fieldList.add(out);
		ClassDecl system = new ClassDecl("System", fieldList, new MethodDeclList(),predefined);
		return system;
	}
	
	
	private ClassDecl createPrintStream() {
		MethodDeclList printList = new MethodDeclList();
		ParameterDeclList printerParameters = new ParameterDeclList();
		ParameterDecl n = new ParameterDecl(new BaseType(TypeKind.INT,predefined),"n",predefined);
		printerParameters.add(n);
		MethodDecl printLN = new MethodDecl(new FieldDecl(false, false, new BaseType(TypeKind.VOID, predefined),"println",predefined), printerParameters, new StatementList(),predefined);
		printList.add(printLN);
		ClassDecl _PrintStream = new ClassDecl("_PrintStream", new FieldDeclList(), printList, predefined);
		return _PrintStream;
	}
	
	private ClassDecl createString() {
		ClassDecl temp = new ClassDecl("String", new FieldDeclList(), new MethodDeclList(), predefined);
		temp.type = new BaseType(TypeKind.UNSUPPORTED, null);
		return temp;
	}
	
	public HashMap<String, Declaration> getPredefinedDecl(){
		return table.get(0);
	}
	
	private void report(int line, String type, String reason) {
		reporter.reportError("*** line " + line + ":  " + type + " error - " + reason);
	}

	
	
}
