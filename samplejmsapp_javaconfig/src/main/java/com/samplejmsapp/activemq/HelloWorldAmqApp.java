package com.samplejmsapp.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


// see the original at https://examples.javacodegeeks.com/enterprise-java/spring/integration/spring-boot-integration-activemq-example/
@SpringBootApplication
public class HelloWorldAmqApp {
 
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldAmqApp.class, args);
    }
}
