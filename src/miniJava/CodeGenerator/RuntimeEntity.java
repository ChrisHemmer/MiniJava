package miniJava.CodeGenerator;

public class RuntimeEntity {

	public int size;
	public int offset;
	
	public RuntimeEntity() {
		this.size = 0;
		this.offset = 0;
	}
	
	public RuntimeEntity(int size, int offset) {
		this.size = size;
		this.offset = offset;
	}
}
