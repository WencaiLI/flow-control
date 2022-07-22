package com.lwc.flowcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class FlowControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowControlApplication.class, args);
    }

}
