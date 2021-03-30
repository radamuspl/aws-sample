package edu.iis.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("edu.iis")
@SpringBootApplication
public class AwsApp {

    public static void main(String[] args) {
        SpringApplication.run(AwsApp.class, args);
    }
}
