import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FilterBuilder {

  public static void main(String[] args) {
    List<Product> products = fetchProducts(); // fetch all products
    Predicate<Product> filter = filter(args); // filter using args
    System.out.println("Filtered by " + Arrays.toString(args)); // print arguments
    System.out.println(productsFilteredBy(filter)); // print filtered products
    System.out.println("All products:\n" + products); // print all products
  }

  private static Predicate<Product> filter(String []args){
    List<Predicate<Product>> predicates = parse(args); // get a list of predicates
    // Reduce the list of predicates using "and"
    // https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
    return predicates.stream().reduce(p -> true, Predicate::and);
  }
  
  private static List<Predicate<Product>> parse(String[] args) {
    List<Predicate<Product>> predicates = new ArrayList<>();
    for (String arg : args) {
      if(arg.split("=").length != 2) {
        continue; // invalid argument!
      }
      try { // Double.parseDouble exception means "skip/continue"
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
      } catch (NumberFormatException nfe) {
        continue;
      }
    }
    return predicates;
  }

  // Alternative version 1: Using removeAll and a predicate (negated)
  private static List<Product> productsFilteredBy(Predicate<Product> filter) {
    List<Product> all = new ArrayList<>(fetchProducts());
    all.removeIf(filter.negate());
    return all;
  }

  // Alternative version 2: Using a stream and filter:
  private static List<Product> productsFilteredBy2(Predicate<Product> filter) {
    return fetchProducts().stream().filter(filter).collect(Collectors.toList());
  }

  private static List<Product> fetchProducts() {
    List<Product> products = new ArrayList<>();
    products.add(new Product("Renat", 100.0, 38.0));
    products.add(new Product("Spendrups", 9.90, 5.6));
    products.add(new Product("Val de Loire", 69.0, 11.0));
    products.add(new Product("Loch Lomond", 249.0, 40.0));
    products.add(new Product("Baily's", 169.0, 30.0));
    return products;
  }
}
/* Class representing a product */
class Product {
  private String name;
  private double price;
  private double alcohol;

  public Product(String name, double price, double alcohol) {
    this.name = name;
    this.price = price;
    this.alcohol = alcohol;
  }

  public String name()    { return name; }
  public double price()   { return price; }
  public double alcohol() { return alcohol; }
  
  public String toString() {
    return name + " " + price + "kr " + alcohol + "%";
  }
}
/*
// Alternative parse loop, using if-statements (could be if-else-ifs for
// efficiency)
for (String arg : args) {
  if (arg.startsWith("max_price=")) {
    double maxPrice = Double.parseDouble(arg.split("=")[1]);
    predicates.add(p -> p.price() <= maxPrice);
  }
  if (arg.startsWith("min_price=")) {
    double minPrice = Double.parseDouble(arg.split("=")[1]);
    predicates.add(p -> p.price() >= minPrice);
  }
  if (arg.startsWith("min_alcohol=")) {
    double minAlcohol = Double.parseDouble(arg.split("=")[1]);
    predicates.add(p -> p.alcohol() >= minAlcohol);
  }
  if (arg.startsWith("max_alcohol=")) {
    double maxAlcohol = Double.parseDouble(arg.split("=")[1]);
    predicates.add(p -> p.alcohol() <= maxAlcohol);
  }
}
*/
