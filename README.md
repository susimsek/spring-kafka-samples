# Spring Kafka Samples
Kafka Samples using Spring Boot

<img src="https://github.com/susimsek/spring-kafka-samples/blob/main/images/introduction.png" alt="Spring Boot Kafka Samples" width="100%" height="100%"/> 

# Kafka

The Spring for Apache Kafka project applies core Spring concepts to the development of Kafka-based messaging solutions. 
We provide a “template” as a high-level abstraction for sending messages.
We also provide support for Message-driven POJOs.

## Prerequisites

* Java 17
* Kotlin
* Maven 3.x
* Kafka


## Build

You can install the dependencies and build by typing the following command

```sh
mvn clean install
```

## Testing

You can run application's tests by typing the following command

```
mvn verify
```


## Code Quality

You can test code quality locally via sonarqube by typing the following command

```sh
mvn -Psonar compile initialize sonar:sonar
```

## Detekt

Detekt a static code analysis tool for the Kotlin programming language

You can run detekt by typing the following command

```sh
mvn antrun:run@detekt
```

## Docker

You can also fully dockerize  the sample applications. To achieve this, first build a docker image of your app.
The docker image of sample app can be built as follows:


```sh
mvn verify jib:dockerBuild
```

# Used Technologies
* Java 17
* Kotlin
* Docker
* Sonarqube
* Detekt
* Checkstyle
* Kafka
* Spring Boot 3.x
* Spring Kafka
* Spring Boot Web
* Spring Boot Validation
* Spring Boot Actuator
* Lombok
