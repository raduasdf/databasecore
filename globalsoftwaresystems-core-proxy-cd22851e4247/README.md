# Micro-service for Web Central UI
## Prerequisite
* Must have:
    * Java JDK 8;
    * Maven 3.5 as a build tool;
    * Running database instance on localhost:8082. Check https://bitbucket.org/globalsoftwaresystems/core-database/src/master/ for instructions.
    * OS: Windows 10 or Linux Debian 8 or above or Ubuntu 16.04 and above;
    * Any IDE will suffice, but the main one we are using is STS/Eclipse IDE.
* Optional:
    * Docker CE.

## Arhitecture description
The main REST API framework that we are using is Spring Boot 1.5.10(Possible update to 2.0.0). This project uses Zuul proxy to send requests to other backend microservices.
For example, if we have a running torsim-database instance, then we can access the api in two ways:
- localhost:8082 - but this will not be exposed;
- localhost:8081/database-api - exposed by Zuul, so security is managed by Zuul as well. Isn't [he](https://vignette.wikia.nocookie.net/ghostbusters/images/3/37/ZuulTerrorDog1.png/revision/latest/scale-to-width-down/1000?cb=20140515200520) scary?

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

2. Run a quick install to see if the project will build:
```bash
$ mvn clean install
// Should build successfully...
$ mvn spring-boot:run
// application should start on localhost:8081, try the following link: http://localhost:8081/swagger-ui.html
```
If everything works acordingly, we can now import this project on your IDE.

## Web developemnt
For the development enviorement, we need to have our microservices running. At the moment we have only the database micro-service ready, so we need this micro service up on localhost:8082 as described above.
Once this service is up and runing, we can go to the frontend project and run the command:
```bash
$ cd frontend/project
$ npm install
// if you have trouble in linux, you can run sudo npm install --usafe-perm
$ ng serve --proxy-config proxy.conf-local.json
// it should start on localhost:4200, and there is a registered proxy that forwards any request from localhost:4200/database-api to localhost:8082
// you can check that by accessing http://localhost:4200/database-api/swagger-ui.html
// this is usefull for http call in Angular
```
There is a demo project where some templates are used and can be usefull for you. To have this demo up and running, do the following:
```bash
$ cd frontend/demo
$ npm install
// if you have trouble in linux, you can run sudo npm install --usafe-perm
$ ng serve --port 4201
// port 4201 is used to not get in the way of 4200 while developing.
```
This is all you need for development.

**DONE**
