package com.example.supplier_gateway.controllers;

import com.example.supplier_gateway.broker.MessageProducer;
import com.example.supplier_gateway.broker.model.Message;
import com.example.supplier_gateway.broker.model.Operation;
import com.example.supplier_gateway.dtos.SupplierRequest;
import com.example.supplier_gateway.dtos.SupplierResponse;
import com.example.supplier_gateway.mappers.SupplierMapper;
import com.example.supplier_service.service.grpc.SupplierOuterClass;
import com.example.supplier_service.service.grpc.SupplierServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.supplier_gateway.configuration.RedisConfig.REDIS_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplier")
@Slf4j
public class GatewayController {

    private final SupplierServiceGrpc.SupplierServiceBlockingStub supplierServiceStub;
    private final SupplierMapper supplierMapper;
    private final MessageProducer messageProducer;

    @GetMapping("/{id}")
    @Cacheable(value = REDIS_KEY, key = "#id")
    public SupplierResponse getById(@PathVariable Long id) {
        log.info("GET request for getting supplier with (id: {})", id);
        SupplierOuterClass.GetSupplierRequest request = supplierMapper.toGetRequest(id);
        SupplierOuterClass.Supplier response = supplierServiceStub.getSupplier(request);
        return supplierMapper.toResponse(response);
    }

    @GetMapping
    @Cacheable(value = REDIS_KEY, key = "#page + '-' + #size")
    public List<SupplierResponse> getPage(@RequestParam int page, @RequestParam int size) {
        log.info("GET request for getting suppliers with (page: {}, size: {})", page, size);
        SupplierOuterClass.GetPageSuppliersRequest request = SupplierOuterClass.GetPageSuppliersRequest.newBuilder().setPage(page).setSize(size).build();
        SupplierOuterClass.SupplierListResponse supplierListResponse = supplierServiceStub.getPageSupplier(request);
        return supplierListResponse.getSuppliersList().stream().map(supplierMapper::toResponse).collect(Collectors.toList());
    }

    @PostMapping
    @CacheEvict(value = REDIS_KEY, allEntries = true)
    public ResponseEntity<?> create(@RequestBody SupplierRequest data) {
        log.info("POST request for creating (supplier: {})", data);
        messageProducer.sendMessage(Message.builder().operation(Operation.CREATE).data(data).build());
        SupplierOuterClass.CreateSupplierRequest request = supplierMapper.toCreateRequest(data);
        SupplierResponse supplierResponse = supplierMapper.toResponse(supplierServiceStub.createSupplier(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierResponse);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = REDIS_KEY, allEntries = true)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SupplierRequest data) {
        log.info("PATCH request for updating (id: {}, supplier: {})", id, data);
        messageProducer.sendMessage(Message.builder().operation(Operation.UPDATE).data(data).build());
        SupplierOuterClass.UpdateSupplierRequest request = supplierMapper.toUpdateRequest(data, id);
        SupplierResponse supplierResponse = supplierMapper.toResponse(supplierServiceStub.updateSupplier(request));
        return ResponseEntity.status(HttpStatus.OK).body(supplierResponse);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = REDIS_KEY, allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("DELETE request for deleting supplier with (id: {})", id);
        messageProducer.sendMessage(Message.builder().operation(Operation.DELETE).data(id).build());
        SupplierOuterClass.GetSupplierRequest request = supplierMapper.toGetRequest(id);
        supplierServiceStub.deleteSupplier(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted.");
    }
}
