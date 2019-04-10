package miniJava.CodeGenerator;


public class UnknownAddress extends RuntimeEntity {

	  public UnknownAddress () {
	    super();
	    address = null;
	  }

	  public UnknownAddress (int size, int level, int displacement) {
	    super (size);
	    address = new ObjectAddress (level, displacement);
	  }

	  public ObjectAddress address;

	}
