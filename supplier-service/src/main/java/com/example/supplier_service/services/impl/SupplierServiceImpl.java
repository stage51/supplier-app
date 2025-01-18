package com.example.supplier_service.services.impl;

import com.example.supplier_service.exceptions.SupplierNotFoundException;
import com.example.supplier_service.models.Supplier;
import com.example.supplier_service.repo.SupplierRepository;
import com.example.supplier_service.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier get(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
    }

    @Override
    @Transactional
    public Supplier update(Supplier supplier, Long id) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        existingSupplier.setName(supplier.getName());
        existingSupplier.setContactPerson(supplier.getContactPerson());
        existingSupplier.setPhone(supplier.getPhone());
        return supplierRepository.save(existingSupplier);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    public Page<Supplier> getPage(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }
}

