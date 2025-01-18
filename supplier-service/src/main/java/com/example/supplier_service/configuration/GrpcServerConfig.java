package com.example.supplier_service.configuration;

import com.example.supplier_service.services.grpc.SupplierGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {
    @Value("${grpc.port}")
    private int grpcPort;

    @Bean
    public Server grpcServer(SupplierGrpcService supplierGrpcService) {
        return ServerBuilder.forPort(grpcPort)
                .addService(supplierGrpcService)
                .build();
    }
}
