#!/bin/bash

echo "Servlet URL: http://localhost:8080/search/products/all?min_alcohol=50&max_price=200&type=Öl&name=Guinness&max_alcohol=60"

echo "Compiling and starting servlet container..."
javac -Xlint:unchecked -cp winstone.jar webroot/WEB-INF/classes/servlets/*.java && java -jar winstone.jar --webroot=webroot
