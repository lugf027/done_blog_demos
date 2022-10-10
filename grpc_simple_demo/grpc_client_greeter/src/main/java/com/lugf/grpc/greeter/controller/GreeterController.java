package com.lugf.grpc.greeter.controller;

import com.lugf.grpc.greeter.service.GreeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreeterController {
    @Autowired
    private GreeterService greeterService;

    @GetMapping("/greeter/{name}")
    public String greeter(@PathVariable String name) {
        return greeterService.sayHello(name);
    }
}
