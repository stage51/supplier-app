package com.example.supplier_gateway.configuration;

import com.example.supplier_service.service.grpc.SupplierServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Value("${grpc.host}")
    private String domainServiceHost;

    @Value("${grpc.port}")
    private int domainServicePort;

    @Bean
    public SupplierServiceGrpc.SupplierServiceBlockingStub courseServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(domainServiceHost, domainServicePort)
                .usePlaintext()
                .build();
        return SupplierServiceGrpc.newBlockingStub(channel);
    }
}

