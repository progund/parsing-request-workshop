How to compile and run:

javac FilterBuilder.java && java FilterBuilder min_alcohol=30

Filters some products on the criteria that alcohol must be at least 30.0%.

To add more criterias, do e.g. the following

javac FilterBuilder.java && java FilterBuilder min_alcohol=30 max_alcohol=38

(Arguments are key-value pairs)
