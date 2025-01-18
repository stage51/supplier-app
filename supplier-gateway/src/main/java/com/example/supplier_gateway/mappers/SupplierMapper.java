package com.example.supplier_gateway.mappers;

import com.example.supplier_gateway.dtos.SupplierRequest;
import com.example.supplier_gateway.dtos.SupplierResponse;
import com.example.supplier_service.service.grpc.SupplierOuterClass;
import org.springframework.stereotype.Component;


@Component
public class SupplierMapper {

    public SupplierOuterClass.CreateSupplierRequest toCreateRequest(SupplierRequest request) {
        return SupplierOuterClass.CreateSupplierRequest.newBuilder()
                .setName(request.getName())
                .setContactPerson(request.getContactPerson())
                .setPhone(request.getPhone())
                        .build();
    }

    public SupplierOuterClass.UpdateSupplierRequest toUpdateRequest(SupplierRequest request, Long id) {
        return SupplierOuterClass.UpdateSupplierRequest.newBuilder()
                .setId(id)
                .setName(request.getName())
                .setContactPerson(request.getContactPerson())
                .setPhone(request.getPhone())
                .build();
    }

    public SupplierOuterClass.GetSupplierRequest toGetRequest(Long id) {
        return SupplierOuterClass.GetSupplierRequest.newBuilder()
                .setId(id)
                .build();
    }

    public SupplierResponse toResponse(SupplierOuterClass.Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactPerson(),
                supplier.getPhone()
        );
    }
}

