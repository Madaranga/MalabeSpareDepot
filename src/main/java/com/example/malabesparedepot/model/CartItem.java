package com.example.malabesparedepot.model;

public class CartItem {
    private Part part;
    private int quantity;

    public CartItem(Part part, int quantity) {
        this.part = part;
        this.quantity = quantity;
    }
    public Part getPart() {
        return part;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //Calculate the base subtotal for specific item
    public double getTotalprice() {
        return part.getPrice() * quantity;
    }
}