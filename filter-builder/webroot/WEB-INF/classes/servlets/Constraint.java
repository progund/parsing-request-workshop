package servlets;

public class Constraint {
  private String field;
  private Type type;
  private String value;
  enum Type {
    MAX,
    MIN,
    EQUALS,
    INVALID;
  }
  public Constraint(String field, Type type, String value) {
    this.field = field;
    this.type = type;
    this.value = value;
  }
  public String field() { return field; }
  public Type type() { return type; }
  public String value() { return value; }
  public String toString() { return field + " " + type + " " + value; }
}
  
