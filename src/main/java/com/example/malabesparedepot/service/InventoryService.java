package com.example.malabesparedepot.service;

import com.example.malabesparedepot.model.Part;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InventoryService {
    public static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    public List<Part> findLowStock(List<Part> parts, int threshold) {
        Objects.requireNonNull(parts, "parts");
        if (threshold < 0) throw new IllegalArgumentException("threshold cannot be negative");
        List<Part> result = new ArrayList<>();
        for (Part part : parts) {
            if (part != null && part.getQuantity() <= threshold) result.add(part);
        }
        return result;
    }

    public void deductStock(Part part, int quantity) {
        Objects.requireNonNull(part, "part");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        if (quantity > part.getQuantity()) throw new IllegalArgumentException("insufficient stock");
        part.setQuantity(part.getQuantity() - quantity);
    }

    public boolean isOutOfStock(Part part) {
        Objects.requireNonNull(part, "part");
        return part.getQuantity() == 0;
    }
}
