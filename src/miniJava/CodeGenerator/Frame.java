package miniJava.CodeGenerator;

public class Frame {

  public Frame() {
    this.level = 0;
    this.size = 0;
  }

  public Frame(int level, Integer size) {
    this.level = level;
    this.size = size.intValue();
  }

  public Frame(int level, int size) {
    this.level = level;
    this.size = size;
  }

  public Frame(Frame frame, int sizeIncrement) {
    this.level = frame.level;
    this.size = frame.size + sizeIncrement;
  }

  public Frame(Frame frame, Integer sizeIncrement) {
    this.level = frame.level;
    this.size = frame.size + sizeIncrement.intValue();
  }

  protected int level;
  protected int size;
}
