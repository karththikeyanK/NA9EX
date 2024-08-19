package com.express.user.service;

import com.express.user.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class ServiceUtil {
    public static <T> T validateEntity(boolean condition, Supplier<T> entitySupplier, String entityName, Long entityId) {
        if (!condition) {
            throw new ResourceNotFoundException(entityName + " with ID " + entityId + " not found");
        }
        return entitySupplier.get();
    }
}
