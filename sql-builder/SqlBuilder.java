import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SqlBuilder {

  private static void checkArgs(String[] args) {
    if (Arrays.asList(args).stream().anyMatch(s -> s.endsWith("="))) {
      System.out.println("Illegal argument: ");
      List<String> illegalArguments = Arrays.asList(args)
        .stream()
        .filter(s -> s.endsWith("="))
        .collect(Collectors.toList());
      System.out.println(illegalArguments);
      System.exit(1);
    }
  }
  
  public static void main(String[] args) {
    System.out.println("Arguments: " + Arrays.toString(args));
    checkArgs(args);
    
    ParameterParser parser =
      new ParameterParser(Arrays.asList(args)
                          .stream()
                          .collect(Collectors.toMap(s -> s.split("=")[0],
                                                    s -> s.split("=")[1])) );
    parser.parse();

    System.out.println("Constraints: " + parser.toString());
    System.out.println("Valid constraints: " + parser.validConstraints());
    System.out.println(parser.areValidConstraints() ?
                       "All constraints are valid" :
                       "Invalid constraints" + parser.invalidConstraints());
    System.out.println("SQL: " + sql(parser.validConstraints()));

  }

  private static String sql(List<Constraint> conds) {
    String condition = "";
    for (Constraint con : conds) {
      switch (con.type()) {
        case MAX:
          condition += con.field() + " <= " + con.value();
          break;
        case MIN:
          condition += con.field() + " >= " + con.value();
          break;
        case EQUALS:
          condition += con.field() + " = '" + con.value() + "'";
          break;
      }
      condition += " AND ";
    }

    if (! condition.equals("")) {
      // Remove last AND, then prepend with WHERE
      condition = condition.substring(0, condition.length()-5);
      condition = " WHERE " + condition;
    }
    return "SELECT * FROM product" + condition + ";";
  }
  
  /** 
   * Interprets the Request object's parameter map
   * (passed to the constructor) and provides a
   * List&lt;Constraint&gt; constraints() method which
   * we can use when getting a list Product filtered
   * by those Constraint objects.
   */
  static class ParameterParser {

    private Map<String, String> map;
    private List<Constraint> constraints;

    private static final String[] validFields = {
      "alcohol",
      "min_alcohol",
      "min_price",
      "max_alcohol",
      "max_price",
      "price",
      "name",
      "type"
    };

    ParameterParser(Map<String, String> map) {
      this.map = map;
      this.constraints = new ArrayList<Constraint>();
    }

    /**
     * Returns the List of valid constraints
     */
    public List<Constraint> validConstraints() {
      List<Constraint> validConstraints = new ArrayList<>(constraints);
      validConstraints.removeIf( c -> c.type() == Constraint.Type.INVALID);
      return validConstraints;
    }

    /**
     * Returns a List&lt;Constraint&gt; with the
     * invalid parameters
     */
    public List<Constraint> invalidConstraints() {
      return constraints
        .stream()
        .filter(c -> c.type() == Constraint.Type.INVALID)
        .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Returns true if all constraints are valid, 
     * false otherwise.
     */
    public boolean areValidConstraints() {
      return constraints.stream().noneMatch( c -> c.type() == Constraint.Type.INVALID);
    }


    private String keyToField(String key) {
      if ( ! Arrays.asList(validFields).contains(key) ) {
        return key;
      }
      return key.matches("^min_.+") || key.matches("^max_.+") ?
        key.split("_")[1] : key;
    }

    private Constraint.Type keyToConstraintType(String key) {
      if ( ! Arrays.asList(validFields).contains(key) ) {
        return Constraint.Type.INVALID;
      }
      return key.matches("^min_.+") ?
        Constraint.Type.MIN :
        key.matches("^max_.+") ? Constraint.Type.MAX :
        Constraint.Type.EQUALS;
    }

    public void parse() {
      for (String key : (Set<String>)map.keySet()) {
        String value = map.get(key); 
        // Remove the min_ or max_ part from the key
        String field = keyToField(key);
        // Is it MAX, MIN or EQUALS?
        Constraint.Type type = keyToConstraintType(key);
        // Store this constraint in the constraints list
        constraints.add(new Constraint(field, type, value));
      }
      constraints.sort((c1, c2) -> c1.field().compareTo(c2.field()));
    }

    public String toString() {
      return constraints.toString();
    }
  }
  
}
