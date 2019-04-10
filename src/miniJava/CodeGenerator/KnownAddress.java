package miniJava.CodeGenerator;


public class KnownAddress extends RuntimeEntity {

	  public KnownAddress () {
	    super();
	    address = null;
	  }

	  public KnownAddress (int size, int level, int displacement) {
	    super(size);
	    address = new ObjectAddress(level, displacement);
	  }

	  public ObjectAddress address;

	}