package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class NullType extends TypeDenoter{

	public NullType(SourcePosition posn){
        super(TypeKind.NULL, posn);
    }
    
    public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitNullType(this, o);
    }
}
