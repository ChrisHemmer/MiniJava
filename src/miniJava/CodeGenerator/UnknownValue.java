package miniJava.CodeGenerator;


public class UnknownValue extends RuntimeEntity {

	  public UnknownValue () {
	    super();
	    address = null;
	  }

	  public UnknownValue (int size, int level, int displacement) {
	    super(size);
	    address = new ObjectAddress(level, displacement);
	  }

	  public ObjectAddress address;

	}
