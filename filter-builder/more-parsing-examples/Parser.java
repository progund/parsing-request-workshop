import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Parser {
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage:");
      System.err.println("java Parser key0=val0 key1=val1 ... keyn=valn");
      System.exit(1);
    }

    // Check that the keys are valid, i.e. are one of
    // min_price, max_price, min_alcohol, max_alcohol
    Set<String> validKeys =
      new HashSet<>(Arrays.asList("min_price",
                                  "max_price",
                                  "min_alcohol",
                                  "max_alcohol",
                                  "name"));
    List<String> validArgs = new ArrayList<>();
    for (String arg : args) {
      String[] keyVal = arg.split("=");
      if (keyVal.length == 2 && validKeys.contains(keyVal[0])) {
        validArgs.add(arg);
      }
    }
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments: " + validArgs);

    // Alternative approach: remove bad arguments by predicate
    validArgs = new ArrayList<>(Arrays.asList(args));
    validArgs
      .removeIf(s -> s.split("=").length != 2 ||
                !validKeys.contains(s.split("=")[0]));
    
    System.out.println("==alternative 2==");
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments: " + validArgs);

    // Alternativ approach: add only valid keys, using stream,filter
    validArgs = Arrays.asList(args)
      .stream()
      .filter(s -> s.split("=").length == 2 &&
                validKeys.contains(s.split("=")[0]))
      .collect(Collectors.toList());
    System.out.println("==alternative 3==");
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments: " + validArgs);

    // Alternative approach: declare a predicate for valid argument
    Predicate<String> isValidArgument =
      s -> s.split("=").length == 2 &&
      validKeys.contains(s.split("=")[0]);
    validArgs = new ArrayList<>(Arrays.asList(args));
    validArgs.removeIf(isValidArgument.negate());
    System.out.println("==alternative 4==");
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments: " + validArgs);

    // Check also that values are doubles
    Predicate<String> hasDoubleValue =
    s -> {
      try {
        Double.parseDouble(s.split("=")[1]);
      } catch (Exception e) {
        return false;
      }
      return true;
    };
    validArgs = new ArrayList<>(Arrays.asList(args));
    validArgs.removeIf(isValidArgument.negate().or(hasDoubleValue.negate()));
    System.out.println("==alternative 5 including double checks==");
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments: " + validArgs);

    // Handle name separately
    validArgs = new ArrayList<>(Arrays.asList(args));
    validArgs.removeIf(isValidArgument.negate());
    Optional<String> name = validArgs.stream()
      .filter(s -> s.startsWith("name=") && s.split("=").length == 2)
      .findFirst();
    System.out.println();
    System.out.println("==alternative 6 including double checks" +
                       " except for name==");
    if (name.isPresent()) {
      System.out.println("Found name parameter: " + name.get());
      System.out.println("Handle name..., then remove it");
      System.out.println("e.g. p -> p.name().contains(name);");
    }
    validArgs.removeIf(hasDoubleValue.negate());
    System.out.println("args: " + Arrays.toString(args));
    System.out.println("Valid arguments (without name): " + validArgs);
    System.out.println("Create predicates for the double values:");
    for (String arg : validArgs) {
      System.out.println("Handling " + arg);
      System.out.println("double value = " +
                         "Double.parseDouble(arg.split(\"=\")[1]);");
      switch(arg.split("=")[0]) {
        case "min_price":
          System.out.println("p -> p.price() >= value");
          break;
        case "max_price":
          System.out.println("p -> p.price() <= value");
          break;
        case "min_alcohol":
          System.out.println("p -> p.alcohol() >= value");
          break;
        case "max_alcohol":
          System.out.println("p -> p.alcohol() <= value");
          break;
        default:
          throw new AssertionError("Parse error");
      }
    }
  }
}
