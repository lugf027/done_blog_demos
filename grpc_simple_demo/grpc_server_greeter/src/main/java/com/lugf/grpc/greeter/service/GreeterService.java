package com.lugf.grpc.greeter.service;

import com.lugf.grpc.greeter.GreeterReply;
import com.lugf.grpc.greeter.GreeterRequest;
import com.lugf.grpc.greeter.GreeterServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class GreeterService extends GreeterServiceGrpc.GreeterServiceImplBase {
    @Override
    public void sayHello(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        log.info("Received Greeter from {}", request.getName());
        GreeterReply greeterReply = GreeterReply.newBuilder().setMessage("Hi, " + request.getName()).build();
        responseObserver.onNext(greeterReply);
        responseObserver.onCompleted();
    }
}

