/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import  miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassDecl extends Declaration {

  public ClassDecl(String cn, FieldDeclList fdl, MethodDeclList mdl, SourcePosition posn) {
	  super(cn, null, posn);
	  fieldDeclList = fdl;
	  methodDeclList = mdl;
	  hasMain = false;
  }
  
  public <A,R> R visit(Visitor<A, R> v, A o) {
      return v.visitClassDecl(this, o);
  }
  
  public int getNumberInstanceFields() {
	  int count = 0;
	  for (FieldDecl fd: fieldDeclList) {
		  if (!fd.isStatic) {
			  count += 1;
		  }
	  }
	  return count;
  }
      
  public FieldDeclList fieldDeclList;
  public MethodDeclList methodDeclList;
  
  public boolean hasMain;
}
