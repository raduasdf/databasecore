# Micro-service for database connection
## Prerequisite
* Must have:
    * Running PostgreSQL database server with database core;
    * Java JDK 8;
    * Maven 3.5 as a build tool;
    * OS: Windows 10 or Linux Debian 8 or above or Ubuntu 16.04 and above;
    * Any IDE will suffice, but the main one we are using is STS/Eclipse IDE.
* Optional:
    * Docker CE.

## Arhitecture description
* The main REST API framework that we are using is Spring Boot 2.0.3, and as a main database, we are using PostgreSQL.
The persistance level is managed by Hibernate with HikariCP as a datasource, and for migration we use FlywayDB to automatically update the database schema.

* The database migration *.sql files should respect the FlywayDB file convention. Please check [here](https://flywaydb.org/documentation/migrations#naming) for more information.
* The current file format is:
    > VX.Y.Z.(TASK#)__(CUSTOM_NAME).sql
    > Where X.Y.Z is the version.

## Instalation
1. Check if you have java and maven on your machine:
    ```bash
    // Maven: 
    $ mvn -v
    Apache Maven 3.5.3 (3383c37e1f9e9b3bc3df5050c29c8aff9f295297; 2018-02-24T21:49:05+02:00)
    // Java
    $ java -version
    java version "1.8.0_161"
    Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
    Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
    
    ```
**NOTE:** You need to have an PostgreSQL server up and running in one of two ways:
* Local server running on localhost:5432, username postgres and password postgres.
* Remote server:
    > You must provide the right host and port in src/main/resources/application.properties 

2. Run a quick install to see if the project will build:
	```bash
	$ mvn clean install
	// Should build successfully...
	$ mvn spring-boot:run
	// application should start on localhost:8082, try the following link: http://localhost:8082/swagger-ui.html
	// Test the devices end-point to check if it works.
	```
If everything works accordingly, we can now import this project on your IDE.

**DONE**
