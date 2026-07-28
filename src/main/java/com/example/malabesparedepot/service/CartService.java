package com.example.malabesparedepot.service;

import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Part;
import java.util.List;
import java.util.Objects;

public final class CartService {
    public void addItem(List<CartItem> cart, Part part, int quantity) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(part, "part");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        CartItem existing = findItem(cart, part);
        int current = existing == null ? 0 : existing.getQuantity();
        if (current + quantity > part.getQuantity()) throw new IllegalArgumentException("insufficient stock");
        if (existing == null) cart.add(new CartItem(part, quantity));
        else existing.setQuantity(current + quantity);
    }

    public boolean removeItem(List<CartItem> cart, Part part) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(part, "part");
        CartItem item = findItem(cart, part);
        return item != null && cart.remove(item);
    }

    public void updateQuantity(List<CartItem> cart, Part part, int quantity) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(part, "part");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        if (quantity > part.getQuantity()) throw new IllegalArgumentException("insufficient stock");
        CartItem item = findItem(cart, part);
        if (item == null) throw new IllegalArgumentException("item is not in cart");
        item.setQuantity(quantity);
    }

    public CartItem findItem(List<CartItem> cart, Part part) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(part, "part");
        for (CartItem item : cart) if (item.getPart() == part) return item;
        return null;
    }
}
