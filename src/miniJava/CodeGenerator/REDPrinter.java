package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.FieldDecl;

public class REDPrinter {

	
	public static void printClassDecl(ClassDecl cd) {
		System.out.println("RED for " + cd.name);
		RuntimeEntity x = cd.RED;
		int length = 1+ x.size * 6;
		printLine(length);
		System.out.print("|");
		char[] middle = new char[length-2];
		
		for (int z=0; z< middle.length; z++) {
			middle[z] = ' ';
		}
		for (int z = 5; z<middle.length; z+= 6) {
			middle[z] = '|';
		}
		
		
		
		for (int i=cd.fieldDeclList.size() - 1; i>=0; i--) {
			FieldDecl fd = cd.fieldDeclList.get(i);
			RuntimeEntity temp = fd.RED;
			int offset = temp.offset;
			//int index = 3+offset*5;
			//System.out.println(index);
			
			middle[2 + offset*6] = fd.name.charAt(0);
		}
		System.out.print(new String(middle) + "|");
		System.out.println();
		printLine(length);
		
	}
	
	public static void printLine(int length) {
		for (int x=0; x < length; x++) {
			System.out.print("=");
		}
		System.out.println("");
	}
	
	public static String printSpace(int length) {
		String result = "";
		for (int x=0; x < length; x++) {
			result += " ";
		}
		return result;
	}
}
