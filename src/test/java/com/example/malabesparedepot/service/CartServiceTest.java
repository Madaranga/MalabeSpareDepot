package com.example.malabesparedepot.service;

import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {
    private final CartService service = new CartService();
    private List<CartItem> cart;
    private Part part;
    @BeforeEach void setUp(){ cart=new ArrayList<>(); part=new Part("P1","Plug","NGK",100,10,"ELECTRICAL","",""); }
    @Test void addsNewItem() { service.addItem(cart,part,2); assertEquals(1,cart.size()); assertEquals(2,cart.getFirst().getQuantity()); }
    @Test void mergesDuplicateProductQuantities() { service.addItem(cart,part,2); service.addItem(cart,part,3); assertEquals(1,cart.size()); assertEquals(5,cart.getFirst().getQuantity()); }
    @Test void rejectsQuantityBeyondAvailableStock() { service.addItem(cart,part,8); assertThrows(IllegalArgumentException.class,()->service.addItem(cart,part,3)); assertEquals(8,cart.getFirst().getQuantity()); }
    @Test void removesExistingItemAndReportsMissingItem() { service.addItem(cart,part,1); assertTrue(service.removeItem(cart,part)); assertFalse(service.removeItem(cart,part)); }
    @Test void updatesQuantityAndSubtotal() { service.addItem(cart,part,1); service.updateQuantity(cart,part,4); assertEquals(4,cart.getFirst().getQuantity()); assertEquals(400,cart.getFirst().getSubtotal(),0.001); }
    @Test void rejectsInvalidQuantityAndMissingItem() {
        assertThrows(IllegalArgumentException.class,()->service.addItem(cart,part,0));
        assertThrows(IllegalArgumentException.class,()->service.addItem(cart,part,-1));
        assertThrows(IllegalArgumentException.class,()->service.updateQuantity(cart,part,1));
    }
    @Test void supportsLargeQuantityWithinStock() { part.setQuantity(1_000_000); service.addItem(cart,part,1_000_000); assertEquals(1_000_000,cart.getFirst().getQuantity()); }
}
