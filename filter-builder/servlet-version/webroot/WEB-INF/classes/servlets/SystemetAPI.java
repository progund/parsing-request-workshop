package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URLDecoder;
import java.util.function.Predicate;

public class SystemetAPI extends HttpServlet{

  @Override
  public void init() throws ServletException {
    // Here we can do some initializations
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    request.setCharacterEncoding(UTF_8.name());
    response.setContentType("text/html;charset=" + UTF_8.name());
    response.setCharacterEncoding(UTF_8.name());
    PrintWriter out = response.getWriter();

    ParameterParser paramParser =
      new ParameterParser(request.getQueryString().split("&"));

    Predicate<Product> filter = paramParser.filter();

    List<Product> products = Products.productsFilteredBy(filter);
    List<Product> allProducts = Products.fetchProducts();

    Formatter formatter = FormatterFactory.getFormatter();
    String json = formatter.format(products);

    StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n")
      .append("<html>\n")
      .append("<head>\n")
      .append("  <title>Minimal REST API</title>\n")
      .append("</head> \n")
      .append("<body>\n")
      .append("<p>All products:</p>")
      .append("<ul>\n");
    for(Product prod : allProducts) {
      sb.append("<li>\n")
        .append(prod)
        .append("\n</li>\n");
    }
    
    sb.append("</ul><p>Products filtered by GET parameters:</p>\n")
      .append("<ul>\n");
    for(Product prod : products) {
      sb.append("<li>\n")
        .append(prod)
        .append("\n</li>\n");
    }
    sb.append("</ul>\n")
      .append(paramParser.invalidArgs().size() != 0 ? "Invalid arguments: " + paramParser.invalidArgs() : "")
      .append("<p>GET-parameters:</p>\n")
      .append("<p>\n")
      .append(request.getQueryString())
      .append(" - as list: ")
      .append(Arrays.toString(request.getQueryString().split("&")))
      .append("</p>\n")
      .append("<p>Json with filtered products:</p>\n")
      .append("<pre>\n")
      .append(json)
      .append("</pre>\n")
      .append("</body>\n")
      .append("</html>\n");
    out.println(sb.toString());
    out.close();
  }
}
