package miniJava.ContextualAnalysis;

import java.util.HashMap;
import java.util.Map;

public class ClassDeclTable {
	
	private Map<String, ClassMemberTable> predefinedTable;
	private Map<String, ClassMemberTable> table;
	
	public ClassDeclTable() {
		predefinedTable = new HashMap<String, ClassMemberTable>();
		table = new HashMap<String, ClassMemberTable>();
	}
	
	public void addClassTable(String name, ClassMemberTable memberTable) {
		table.put(name,  memberTable);
	}
	
	public void addPredefinedClassTable(String name, ClassMemberTable memberTable) {
		predefinedTable.put(name,  memberTable);
	}
	
	public ClassMemberTable getClassTable(String name) {
		ClassMemberTable result =  table.get(name);
		if (result != null) {
			return result;
		} else {
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~RETRIEVING PREDEFINED VALUE");
			return predefinedTable.get(name);
		}
	}
	
	public boolean containsClassTable(String name) {
		return table.containsKey(name) || predefinedTable.containsKey(name);
	}
	
	public String[] getClasses() {
		String[] tableList =  table.keySet().toArray(new String[0]);
		return tableList;
	}
}
