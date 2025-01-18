package com.example.supplier_service.services;

import com.example.supplier_service.models.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    Supplier create(Supplier supplier);
    Supplier get(Long id);
    Supplier update(Supplier supplier, Long id);
    void delete(Long id);
    Page<Supplier> getPage(Pageable pageable);
}
