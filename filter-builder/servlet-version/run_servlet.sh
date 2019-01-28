#!/bin/bash

echo "Test with this URL: http://localhost:8080/search/products/all?min_alcohol=30&max_alcohol=38&apa=bepa&max_price=&type=groda"
echo "Compiling and starting servlet container..."
javac -Xlint:unchecked -cp winstone.jar webroot/WEB-INF/classes/servlets/*.java && java -jar winstone.jar --webroot=webroot
