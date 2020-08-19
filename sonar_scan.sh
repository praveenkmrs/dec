echo "Scanning Customers API"
mvn sonar:sonar \
  -Dsonar.projectKey=cde-customer \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=64178b9af21a3a6799ddee64adc4fd7a8abbf871 -f customers/pom.xml
echo "Scanning Customers API complete"
echo "Scanning Cards API"
 mvn sonar:sonar \
  -Dsonar.projectKey=cde-cards \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=b6b8cfa6a92e2a08442d54030d91d1f133ec981b -f cards/pom.xml
echo "Scanning Cards API complete"
echo "Scanning Payments API"
mvn sonar:sonar \
  -Dsonar.projectKey=cde-payment \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=b45efe935df691a5dcd031461ec3c9670cf3a823 -f payments/pom.xml
  echo "Scanning Payments API complete"