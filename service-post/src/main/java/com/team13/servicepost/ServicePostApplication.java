package com.team13.servicepost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ServicePostApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePostApplication.class, args);
    }

}
