package com.example.malabesparedepot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {
    private Part part() { return new Part("P1", "Plug", "NGK", 100.0, 10, "ELECTRICAL", "2026-01-01", ""); }

    @Test void constructorCalculatesSubtotal() {
        CartItem item = new CartItem(part(), 3);
        assertEquals(300.0, item.getSubtotal(), 0.001);
    }

    @Test void setQuantityRecalculatesSubtotal() {
        CartItem item = new CartItem(part(), 1);
        item.setQuantity(4);
        assertEquals(4, item.getQuantity());
        assertEquals(400.0, item.getTotalprice(), 0.001);
    }
}
