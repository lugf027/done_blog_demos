package com.lugf.grpc.greeter.service;

import com.lugf.grpc.greeter.GreeterReply;
import com.lugf.grpc.greeter.GreeterRequest;
import com.lugf.grpc.greeter.GreeterServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GreeterService {
    @GrpcClient(value = "grpc_service_greeter")
    private GreeterServiceGrpc.GreeterServiceBlockingStub stub;

    public String sayHello(String name) {
        GreeterReply reply = stub.sayHello(GreeterRequest.newBuilder().setName(name).build());
        return reply.getMessage();
    }
}
