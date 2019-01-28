# parsing-request-workshop
Workshop for the Exposing data over HTTP assignment - How to parse the GET parameters

[From the assignment called Exposing data over HTTP - Lab 1](http://wiki.juneday.se/mediawiki/index.php/Assignment:Exposing_data_over_http_lab1_Web_API)

The purpose of this workshop is to present various techniques for parsing the GET parameters in a Servlet in order
to sort a list of, in this case, Product references.

The idea is to look at the GET parameters like `min_price=20&max_price=30` and from those create some
kind of filter. The filter should allow the servlet to fetch and serve only products matching the criteria
of, in this case, having a price between 20 and 30.

First we show how to create an SQL SELECT statement from the parameters (creating an appropriate WHERE clause).

Next, we show how to create a `Predicate<Product>` to use as a filter to get a list of only such products that
satisfy the filter.
