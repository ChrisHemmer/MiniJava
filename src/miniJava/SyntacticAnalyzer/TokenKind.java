/**
 *   TokenKind is a simple enumeration of the different kinds of tokens
 *   
 */
package miniJava.SyntacticAnalyzer;

public enum TokenKind {
	// Identifier (DONE)
	IDENTIFIER,
	
	// Number (DONE)
	NUM,
	
	// Operator Types (Might not need this)
	BINOP,
	UNOP,
	
	// COMMENT TYPES (Probably don't need this)
	COMMENT,
	STARTCOMMENT,
	ENDCOMMENT,
	COMMENTTEXT,
	
	// Relational Operators (DONE)
	LT,
	GT,
	EQUALS,
	LTEQ,
	GTEQ, 
	NEQ,
	
	// Logical Operators (DONE)
	AND,
	OR,
	NOT,
	
	// Arithmetic Operators (DONE)
	PLUS,
	MINUS,
	TIMES,
	DIVIDE,
	
	// Keywords (DONE)
	IF,
	ELSE, 
	CLASS,
	VOID,
	PUBLIC,
	PRIVATE,
	STATIC,
	INT,
	BOOLEAN,
	RETURN,
	TRUE,
	FALSE,
	NEW,
	WHILE,
	THIS,
	
	// Groupings (DONE)
	LPAREN,
	RPAREN,
	LBRACKET,
	RBRACKET,
	LBRACE,
	RBRACE,
	
	// Basic Punctuation (DONE)
	DOT,
	COMMA,
	SEMICOLON,
	
	// Assignment (DONE)
	ASSIGNMENT,
	
	// Misc. (DONE)
	EOT,
	ERROR,
	NULL
}
