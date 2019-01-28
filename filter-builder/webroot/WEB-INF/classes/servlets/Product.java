package servlets;

public class Product {
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
