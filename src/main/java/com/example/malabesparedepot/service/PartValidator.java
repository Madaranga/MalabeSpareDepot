package com.example.malabesparedepot.service;

public final class PartValidator {
    public void validate(String code, String name, String brand, double price, int quantity, String category) {
        requireText(code, "code"); requireText(name, "name"); requireText(brand, "brand"); requireText(category, "category");
        if (!Double.isFinite(price) || price < 0) throw new IllegalArgumentException("price must be a non-negative finite number");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
    }
    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
    }
}
