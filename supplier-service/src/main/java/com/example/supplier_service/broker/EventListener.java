package com.example.supplier_service.broker;

import com.example.supplier_service.broker.model.Message;
import com.example.supplier_service.broker.model.Operation;
import com.example.supplier_service.services.SupplierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final ObjectMapper objectMapper;
    private final SupplierService supplierService;

    @RabbitListener(queues = "${rabbit.queue}")
    public void handleEvent(String stringMessage) {
        Message message = convertToObject(stringMessage);
        Operation operation = message.getOperation();
        try {
            if (Operation.DELETE.equals(operation)) {
                handleDelete(Long.valueOf((Integer) message.getData()));
            }
        } catch (Exception ignored) {
        }
    }

    private Message convertToObject(String message) {
        try {
            return objectMapper.readValue(message, Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDelete(Long id) {
        supplierService.delete(id);
    }

}
