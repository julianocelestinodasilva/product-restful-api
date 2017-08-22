- Build Application and run Tests :

To build and start application run mvn install (or mvn clean install);
The tests will be run in the Maven tests phase (except the tests that are in the package br.com.productrestfulapi.acceptancetests);
To execute the acceptance tests, run JUnit in the package br.com.productrestfulapi.acceptancetests;

- Configure url (directory) Database :

Configure database directory (if linux system, it is not necessary. Database directory is default in the home user directory) :
If the system isn't Linux, change the database directory in the File "conexao.properties" (src/main/resources) - Property "bd.productrestfulapi.url";
