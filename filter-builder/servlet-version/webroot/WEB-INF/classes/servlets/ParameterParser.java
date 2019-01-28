package servlets;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParameterParser {
  private String[] args;
  public ParameterParser(String[] args) {
    this.args = args;
  }

  public Predicate<Product> filter() {
    List<Predicate<Product>> predicates = parse(); // get a list of predicates
    // Reduce the list of predicates using "and"
    // https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
    return predicates.stream().reduce(p -> true, Predicate::and);
  }
  
  private static boolean isDouble(String value) {
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }

  private boolean isValidKey(String key) {
    return key.split("=").length==2 &&
      Arrays.stream(new String[] {
          "min_price",
          "max_price",
          "min_alcohol",
          "max_alcohol"
        }).collect(Collectors.toList()).contains(key.split("=")[0]);
  }
  
  private List<Predicate<Product>> parse() {
    List<Predicate<Product>> predicates = new ArrayList<>();
    List<String> validArgs = new ArrayList<>(Arrays.asList(args));
    validArgs.removeIf(s->!isValidKey(s) || !isDouble(s.split("=")[1]));
    for (String arg : validArgs) {
      double value = Double.parseDouble(arg.split("=")[1]);
      switch(arg.split("=")[0]) { // Check what filter it is
        case "max_price": predicates.add(p -> p.price() <= value);
          break;
        case "min_price": predicates.add(p -> p.price() >= value);
          break;
        case "min_alcohol": predicates.add(p -> p.alcohol() >= value);
          break;
        case "max_alcohol": predicates.add(p -> p.alcohol() <= value);
          break;
        default:
          continue;
      }
    }
    return predicates;
  }

  public List<String> invalidArgs() {
    List<String> invalids = Arrays.stream(args)
      .filter(a->!isValidKey(a) || !isDouble(a.split("=")[1]))
      .collect(Collectors.toList());
    return invalids;
  }
}
