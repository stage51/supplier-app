package com.example.supplier_service.rabbit;

import com.example.supplier_service.models.Supplier;
import com.example.supplier_service.rabbit.model.Message;
import com.example.supplier_service.rabbit.model.Operation;
import com.example.supplier_service.services.SupplierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

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
            if (Operation.CREATE.equals(operation)) {
                handleCreate(convertToSupplier(message.getData()));
            } else if (Operation.UPDATE.equals(operation)) {
                handleUpdate(convertToSupplier(message.getData()));
            } else if (Operation.DELETE.equals(operation)) {
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

    private Supplier convertToSupplier(Object object) {
        Map<String, Object> data = (Map) object;
        return objectMapper.convertValue(data, Supplier.class);
    }

    private void handleCreate(Supplier supplier) {
        if (supplier.getId() != null) {
            return;
        }
        supplierService.create(supplier);
    }

    private void handleUpdate(Supplier supplier) {
        supplierService.update(supplier, supplier.getId());
    }

    private void handleDelete(Long id) {
        supplierService.delete(id);
    }

}
