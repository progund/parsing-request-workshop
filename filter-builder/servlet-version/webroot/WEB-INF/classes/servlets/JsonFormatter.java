package servlets;

import java.util.List;

public class JsonFormatter implements Formatter {
  @Override
  public String format(List<Product> products) {
    StringBuilder json = new StringBuilder("[\n");
    int lastIndex = products.size() - 1;
    int index = 0;
    for (Product product : products) {
      json.append("  {\n")
        .append("    name:\"").append(product.name()).append("\",\n")
        .append("    price:").append(product.price()).append(",\n")
        .append("    alcohol:").append(product.alcohol()).append("\n")
        .append("  }");
      if (index != lastIndex) {
        json.append(","); // all but the last product gets a comma
      }
      json.append("\n");
      index++;
      
    }
    json.append("]\n");
    return json.toString();
  }
}
