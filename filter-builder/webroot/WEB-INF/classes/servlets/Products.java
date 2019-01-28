package servlets;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class Products {

  public static List<Product> productsFilteredBy(Predicate<Product> filter) {
    return fetchProducts().stream().filter(filter).collect(Collectors.toList());
  }  

  public static List<Product> fetchProducts() {
    List<Product> products = new ArrayList<>();
    products.add(new Product("Renat", 100.0, 38.0));
    products.add(new Product("Spendrups", 9.90, 5.6));
    products.add(new Product("Val de Loire", 69.0, 11.0));
    products.add(new Product("Loch Lomond", 249.0, 40.0));
    products.add(new Product("Baily's", 169.0, 30.0));
    return products;
  }
}
