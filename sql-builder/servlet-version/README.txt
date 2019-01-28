To start the servlet container for the SQL builder:

./clean.sh && ./run_servlet.sh

Try the following URL for building an SQL statement of only valid parameters:

 http://localhost:8080/search/products/all?min_alcohol=50&max_price=200&type=Öl&name=Guinness&max_alcohol=60

Try the following for some invalid parameters as well:

 http://localhost:8080/search/products/all?min_alcohol=50&max_price=200&type=%C3%96l&name=Guinness&max_alcohol=60&apa=bepa


