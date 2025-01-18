package com.example.supplier_gateway.dtos;

import lombok.Data;

@Data
public class SupplierRequest {
    private String name;
    private String contactPerson;
    private String phone;
}
