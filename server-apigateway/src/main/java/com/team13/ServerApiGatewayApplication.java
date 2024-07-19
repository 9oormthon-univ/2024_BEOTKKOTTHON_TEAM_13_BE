package com.team13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServerApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApiGatewayApplication.class, args);
    }
}
