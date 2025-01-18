package com.example.supplier_gateway.broker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private Operation operation;
    private Object data;
}
