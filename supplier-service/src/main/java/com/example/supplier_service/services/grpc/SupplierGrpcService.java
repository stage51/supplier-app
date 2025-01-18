package com.example.supplier_service.services.grpc;

import com.example.supplier_service.models.Supplier;
import com.example.supplier_service.service.grpc.SupplierOuterClass;
import com.example.supplier_service.service.grpc.SupplierServiceGrpc;
import com.example.supplier_service.services.SupplierService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class SupplierGrpcService extends SupplierServiceGrpc.SupplierServiceImplBase {

    private final SupplierService supplierService;

    @Autowired
    public SupplierGrpcService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public void createSupplier(SupplierOuterClass.CreateSupplierRequest request, StreamObserver<SupplierOuterClass.Supplier> responseObserver) {
        try {
            Supplier supplier = new Supplier();
            supplier.setName(request.getName());
            supplier.setContactPerson(request.getContactPerson());
            supplier.setPhone(request.getPhone());

            Supplier createdSupplier = supplierService.create(supplier);

            SupplierOuterClass.Supplier response = toGrpcSupplier(createdSupplier);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getPageSupplier(SupplierOuterClass.GetPageSuppliersRequest request, StreamObserver<SupplierOuterClass.SupplierListResponse> responseObserver) {
        try {
            Page<Supplier> suppliersPage = supplierService.getPage(PageRequest.of(request.getPage(), request.getSize()));

            SupplierOuterClass.SupplierListResponse.Builder responseBuilder = SupplierOuterClass.SupplierListResponse.newBuilder();
            suppliersPage.forEach(supplier -> responseBuilder.addSuppliers(toGrpcSupplier(supplier)));

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getSupplier(SupplierOuterClass.GetSupplierRequest request, StreamObserver<SupplierOuterClass.Supplier> responseObserver) {
        try {
            Supplier supplier = supplierService.get(request.getId());

            SupplierOuterClass.Supplier response = toGrpcSupplier(supplier);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateSupplier(SupplierOuterClass.UpdateSupplierRequest request, StreamObserver<SupplierOuterClass.Supplier> responseObserver) {
        try {
            Supplier supplier = new Supplier();
            supplier.setName(request.getName());
            supplier.setContactPerson(request.getContactPerson());
            supplier.setPhone(request.getPhone());

            Supplier updatedSupplier = supplierService.update(supplier, request.getId());

            SupplierOuterClass.Supplier response = toGrpcSupplier(updatedSupplier);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteSupplier(SupplierOuterClass.GetSupplierRequest request, StreamObserver<SupplierOuterClass.Empty> responseObserver) {
        try {
            supplierService.delete(request.getId());
            responseObserver.onNext(SupplierOuterClass.Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    private SupplierOuterClass.Supplier toGrpcSupplier(Supplier supplier) {
        return SupplierOuterClass.Supplier.newBuilder()
                .setId(supplier.getId())
                .setName(supplier.getName())
                .setContactPerson(supplier.getContactPerson())
                .setPhone(supplier.getPhone())
                .build();
    }
}
