package com.example.supplier_service.rabbit.model;

import lombok.Data;

@Data
public class Message {
    private Operation operation;
    private Object data;
}
