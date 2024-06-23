# Product middleware
REST middleware API that displays products with advanced filtering capabilities.
Middleware interacts with data from dummyjson.com and an H2 database.
It exposes several endpoints to manage product data, including retrieving, filtering, and searching for products.
The application is built using Spring Boot and leverages Spring Data JPA for database interactions.

## Table of contents
* [Key features](#Key-features)<br>
* [Configuration](#Configuration)<br>
* [Run application](#Running-the-application)<br>
        1. [IntelliJ](#Running-Locally-Through-IntelliJ-IDEA (Recommended))<br>
        2. [Docker](#Running-with-Docker)<br>      
- [Testing](#Testing)<br>
        1. [Authorization](#Authorization)<br>
        2. [Swagger-UI](#Swagger-UI)<br>
        3. [Postman](#Postman)<br>
        4. [Unit and integration tests](#Unit-and-integration-tests)<br>
- [Technology stack](#Technology-stack)                 

## Key features
- Product Management: Endpoints for retrieving all products, filtering by category and price, and searching by title.
- Database: Uses H2 database for data storage.
- Caching: Implements caching for product filtering and searching to improve performance.
- Logging: Comprehensive logging with info, warning, error and debug.
- Swagger UI: API documentation and testing available through Swagger UI.
- Authentication and Authorization: Secured endpoints with token-based authentication and authorization.

## Configuration

Prerequisites:
-  Java 21
-  Gradle
-  Docker (optional)

Application uses H2 database. The configuration for database is specified in the **application.properties** file.
There is no need for aditional setup for H2 database since it will be automatically configured.

        spring.datasource.url=jdbc:h2:file:~/middlewareDB
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=password
        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

## Running the application

First thing you need to do is **clone GitHub repository** and checkout on master branch.

Application can run with docker or without docker. I recommend running without docker because it is more time efficient, but I implemented **docker-compose.yml** and Dockerfile for easier future deployment and scalability. Both ways are described below.

### Running Locally Through IntelliJ IDEA (Recommended)
To run application with IntelliJ IDEA you need to select main class as configuration (like on the picture).
Main class is located at **java/com/example/middleware/MiddlewareApplication.java**

After that you simply click run or with keys Shift + F10.

![Intellij IDEA running application](https://github.com/BrunoPavlovic/product-middleware/assets/100703459/19ed1e9e-ed6c-44e3-b2e6-cc761076e2fd)

## Running with Docker
To run application with docker first you need to **build the JAR file**. You can do it on 2 ways:
  1. Trough IntelliJ - open Gradle view -> Tasks -> build -> Double click on build
     ![Build gradle](https://github.com/BrunoPavlovic/product-middleware/assets/100703459/6bac376b-d2f8-43a5-8a78-306ad642a679)

  2. With command (executed from root directory ) : ./gradlew build

From root directory run command : **docker-compose up -d --build**


## Testing 
After the application is started you can access it on: http://localhost:8080/

### Authorization
Every endpoint in application is protected except /login route.
On route: [http://localhost:8080/api/v1/auth/login](http://localhost:8080/api/v1/auth/login) you need to send POST request with body.<br>
**Body:**

        {
          "username":"emilys",
          "password":"emilyspass"
        }

You will get response that include "token" atribute and you need to copy it (with that token you will authorize request while testing application).

### Swagger-UI
Ednpoints are documented with swagger. You can see Response status, expected parameters, descriptions and other details.

With swagger testing is easy, in endpoint details there is button **Try it out** and when you click it you can execute request ( when you define parameters if needed ofcourse ).

Test application with swagger-ui on: [Click here](http://localhost:8080/swagger-ui/index.html#/)

1. If you already aren't look at [Authorization](#Authorization) and send request so you get token.
2. Before documented endpoints there is button Authorize -> click it -> input **Bearer <your_token_here>**
3. Now you can send any request with Try it out ( remark : you need to input anything in authorization because functions in controller have annotation @Request Header) -> so you simply write for example Bearer and send request and you will see in curl command that Swagger used token you define in second step and not the one you provided as parameter ).

### Postman
I have preapred Postman Workspace for testing the API. [Workspace](https://app.getpostman.com/join-team?invite_code=d835f3834911b6c14b292ddbeab18e23&target_code=8a62904c9ab51154f401740baafde1b3).
For every endpoint there is predefined request.

Using postman:
1. Get token the same way as desribed [here](#Authorization)
2. When sending request you can authorize request on 2 ways:
   - Authorization -> Select auth type as Bearer token -> paste token as value
   - Headers -> Header key: Authorization -> Header value: Bearer <your_token>

### Unit and integration tests
Currently, tests are developed for ProductRepository, ProductService, and ProductController.<br>
You can run them by right-clicking on the test class ( src/test/java/com/example/middleware/)  and selecting Run, or you can run individual tests directly from their respective test classes.

## Technology stack
- Framework: Spring Boot
- Database: H2 Database
- ORM: JPA
- Caching: Spring Cache
- Logging: Apache Log4j
- API Documentation: Swagger UI
- Containerization: Docker

