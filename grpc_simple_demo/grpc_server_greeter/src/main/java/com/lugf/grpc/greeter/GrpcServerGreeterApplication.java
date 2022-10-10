package com.lugf.grpc.greeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GrpcServerGreeterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcServerGreeterApplication.class, args);
    }
}
