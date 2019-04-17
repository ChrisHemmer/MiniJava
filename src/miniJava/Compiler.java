/**
 *   COMP 520 
 *   miniJava scanner and parser
 *
 *  Parser grammar:
 *  Program ::= (ClassDeclaration)*eot
 *	ClassDeclaration ::= class id { ( FieldDeclaration | MethodDeclaration )* } FieldDeclaration ::= Visibility Access Type id ;
 *	MethodDeclaration ::= Visibility Access (Type|void)id(ParameterList?){Statement*} Visibility ::= ( public | private )?
 *	Access ::= static ?
 *	Type ::= int | boolean | id | (int|id)[]
 *	ParameterList ::= Type id ( , Type id )*
 *	ArgumentList ::= Expression(,Expression)*
 *	Reference ::= id | this | Reference.id
 *	IxReference ::= Reference [ Expression ]
 *	Statement ::=
 *		{ Statement* }
 *		| Typeid=Expression;
 *		| ( Reference | IxReference ) = Expression ;
 *		| Reference (ArgumentList?);
 *		| return Expression? ;
 *		| if ( Expression ) Statement (else Statement)? | while ( Expression ) Statement
 *	Expression ::= Reference
 *		| IxReference
 *		| Reference(ArgumentList?)
 *		| unop Expression
 *		| Expression binop Expression
 *		| ( Expression )
 *		| num | true | false
 *		| new (id() | int[Expression] | id[Expression])
 */

package miniJava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import mJAM.Disassembler;
import mJAM.Interpreter;
import mJAM.ObjectFile;
import miniJava.AbstractSyntaxTrees.AST;
import miniJava.AbstractSyntaxTrees.ASTDisplay;
import miniJava.CodeGenerator.CodeGenerator;
import miniJava.ContextualAnalysis.Identification;
import miniJava.ContextualAnalysis.TypeChecking;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.Scanner;

/**
 * Recognize whether input entered through the keyboard is a valid
 * arithmetic expression as defined by the simple CFG and scanner grammar above.  
 * 
 */
public class Compiler {

	static boolean printCommands = true;
	
	public static void main(String[] args) {

		InputStream inputStream = null;
		
		
		
		
		try {
			inputStream = new FileInputStream(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("Input file " + args[0] + " not found");
			System.exit(3);
		}
		
		ErrorReporter reporter = new ErrorReporter();
		Scanner scanner = new Scanner(inputStream, reporter);
		Parser parser = new Parser(scanner, reporter);

		println("Syntactic analysis ... ");
		AST ast = parser.parse();
		print("Syntactic analysis complete:  ");
		
		// START: Checking results of parsing
		if (reporter.hasErrors()) {
			println("Syntactically Invalid miniJava program");
			System.exit(4);
		} else {
			println("Syntactically Valid miniJava program");
		}
		// END: Checking results of parsing
		
		println(inputStream.toString());
		
		
		println("Contextual analysis ... ");
		
		
		// START: Checking results of identification
		new Identification(ast, reporter);
		print("Contextual analysis - identification complete: ");
		if (reporter.hasErrors()) {
			println("Failed Identification");
			System.exit(4);
		}
		else {
			println("Passed Identification");
		}
		
		// END: Checking results of identification
		
		
		
		
		
		// START: Checking results of Type Checking
		new TypeChecking(ast, reporter);
		print("Contextual analysis - type checking complete: ");
		if (reporter.hasErrors()) {
			println("Failed Type Checking");
			System.exit(4);
		}
		else {
			println("Passed Type Checking");
			println("Valid miniJava program");
			//new ASTDisplay().showTree(ast);
		}
		new CodeGenerator().encodeRun(ast);
		new ObjectFile("test.mJAM").write();
		
		
		println("\n\nDisassembling...");
		
		if (printCommands) {
			new Disassembler("test.mJAM").disassemble();
			println("\n\n\n");
			println("Running...");
		}
		
		Interpreter.interpret("test.mJAM");
		// END: Checking results of Type Checking 
			
	}
	
	public static void println(String str) {
		if (printCommands) {
			System.out.println(str);
		}
	}
	
	public static void print(String str) {
		if (printCommands) {
			System.out.print(str);
		}
	}
}







