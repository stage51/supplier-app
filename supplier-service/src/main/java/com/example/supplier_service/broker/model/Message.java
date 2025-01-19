package com.example.supplier_service.broker.model;

import lombok.Data;

@Data
public class Message {
    private Operation operation;
    private Object data;
}
