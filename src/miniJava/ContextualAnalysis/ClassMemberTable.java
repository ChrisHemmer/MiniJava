package miniJava.ContextualAnalysis;

import java.util.HashMap;
import java.util.Map;

import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.MemberDecl;
import miniJava.AbstractSyntaxTrees.MethodDecl;

public class ClassMemberTable {
	private Map<String, Declaration> publicMembers;
	private Map<String, Declaration> privateMembers;
	private Map<String, Declaration> staticMembers;
	
	private int numFields;
	private int numMethods;
	
	public ClassMemberTable(ClassDecl classDecl) {
		publicMembers = new HashMap<String, Declaration>();
		privateMembers = new HashMap<String, Declaration>();
		staticMembers = new HashMap<String, Declaration>();
		numFields = 0;
		numMethods = 0;
		for (FieldDecl fd: classDecl.fieldDeclList) {
			addMember(fd);
			numFields ++;
		}
		
		for (MethodDecl md: classDecl.methodDeclList) {
			addMember(md);
			numMethods++;
		}
	}
	
	public boolean contains(String name) {
		return staticContains(name) || publicContains(name) || privateContains(name);
	}
	
	public boolean staticContains(String name) {
		return staticMembers.containsKey(name);
	}
	
	public boolean publicContains(String name) {
		return publicMembers.containsKey(name);
	}
	
	public boolean privateContains(String name) {
		return privateMembers.containsKey(name);
	}
	
	public void addMember(MemberDecl member) {
		if (member.isStatic) {
			staticMembers.put(member.name, member);
		}
		if (member.isPrivate) {
			privateMembers.put(member.name, member);
		} else {
			publicMembers.put(member.name, member);
		}
	}
	
	public Declaration getPublic(String name) {
		if (publicMembers.containsKey(name)) {
			return publicMembers.get(name);
		}
		return null;
	}
	
	public Declaration getPrivate(String name) {
		if (privateMembers.containsKey(name)) {
			return privateMembers.get(name);
		}
		return null;
	}
	
	public Declaration getStatic(String name) {
		if (staticMembers.containsKey(name)) {
			return staticMembers.get(name);
		}
		return null;
	}
	
	public Declaration getPublicStatic(String name) {
		if (staticMembers.containsKey(name) && publicMembers.containsKey(name)) {
			return staticMembers.get(name);
		}
		return null;
	}

	
	public Declaration get(String name) {
		if (publicMembers.containsKey(name)) {
			return publicMembers.get(name);
		}
		if (privateMembers.containsKey(name)) {
			return privateMembers.get(name);
		}
		if (staticMembers.containsKey(name)) {
			return staticMembers.get(name);
		}
		return null;
	}
	
	public int size() {
		return numMethods + numFields;
	}
	
}
