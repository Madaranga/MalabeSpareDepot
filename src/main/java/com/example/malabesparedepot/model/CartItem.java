package com.example.malabesparedepot.model;

public class CartItem {
    private final Part part;
    private int quantity;
    private double subtotal;

    public CartItem(Part part, int quantity) {
        this.part = part;
        this.quantity = quantity;
        this.subtotal = part.getPrice() * quantity;
    }
    public Part getPart() {
        return part;
    }
    public String getPartCode() {
        return part.getPartCode();
    }
    public String getName() {
        return part.getName();
    }
    public double getPrice() {
        return part.getPrice();
    }


    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.part.getPrice() * quantity;
    }

    //Calculate the base subtotal for specific item
    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalprice() {
        return subtotal;
    }
}