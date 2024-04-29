# Licious Order Management System
<!-- ABOUT THE PROJECT -->
## About The Project
The Licious Order Management System (LOMS) is a comprehensive platform designed to streamline and
optimize the order processing workflow for Licious, a leading online meat and seafood delivery service.
LOMS provides a user-friendly interface for managing orders from the point of placement to delivery.

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 3](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/products/docker-desktop/)

## Running the application locally

* #### clone the repository into your local computer

```shell
    git clone https://github.com/codecoder005/licious-order-management-system.git
```
* #### Open the project using IntelliJ or any IDE and Get inside the project using Terminal
```shell
    cd licious-order-management-system
```
* #### Package the code to make sure all the dependencies are downloaded successfully
```shell
    mvn clean package
```

* #### Make sure you have docker,docker-compose installed and docker is up and running. Then run the docker-compose.yml file which will fire up a mysql container. make sure you are in root directory of the project in terminal
```shell
    docker-compose up -d
```

* #### Our Project need these below environment variables to run.

1. DB_JDBC_URL example:  jdbc:mysql://localhost:3306/licious_oms?createDatabaseIfNotExist=true;
2. DB_USERNAME example: root;
3. DB_PASSWORD example: password;
4. RUNNING_ENVIRONMENT example: it should be local, dev, stage or prod

* #### Finally run the `LiciousOMSApplication`
* #### FYI, the application will automatically create tables 

## Testing APIs using swagger
* #### I have added swagger dependency in the project pom.
* You can go to `http://localhost:8080/swagger-ui/index.html#/` and play with the APIs





## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecoder005/licious-order-management-system/blob/master/LICENSE) file.