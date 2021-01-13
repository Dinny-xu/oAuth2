package com.study.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class WebGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.study.web.WebGateWayApplication.class, args);
    }
}