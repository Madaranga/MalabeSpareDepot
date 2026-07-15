package com.example.malabesparedepot;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.util.*;

import java.util.ArrayList;
import java.util.List;
import com.example.malabesparedepot.model.CartItem;


public class TestApp {
    public static void main(String[] args) {
        System.out.println("======Testing POS Logic & Audit Logger======");

        List<Part> inventory = DataParser.parseInventory("inventory_legacy.txt");
        if (inventory.isEmpty()) {
            System.err.println("No inventory found!. inventory_legacy.txt is EMPTY!!!");
            return;
        }


        Part part1 = inventory.get(0);
        Part part2 = inventory.size() > 1 ? inventory.get(1) : part1;

        System.out.println("Selected Part 1: " + part1.getName() + " | Category: " + part1.getCategory() + " | Price: Rs." + part1.getPrice());
        System.out.println("Selected Part 2: " + part2.getName() + " | Category: " + part2.getCategory() + " | Price: Rs." + part2.getPrice());

        //Create cart and add items
        List<CartItem> cart = new ArrayList<>();

        //Add 3 5% bulk discount
        cart.add(new CartItem(part1, 3));
        LoggerUtil.logAction("ADD_TO_CART", part1.getPartCode(),3);

        //Add 1 of Part 2
        cart.add(new CartItem(part2, 1));
        LoggerUtil.logAction("ADD_TO_CART", part2.getPartCode(),1);

        //Calculate total price
        double finalTotal = PriceCalculator.calculateFinalTotal(cart);
        System.out.println("\n---Cart Math Verification---");
        System.out.println("Item 1 Subtotal (QTY 3): Rs." + (part1.getPrice()*3));
        System.out.println("Item 1 with 5% Discount: Rs." + (part1.getPrice()*3*0.95));
        System.out.println("Item 2 Subtotal (QTY 1): Rs." + part2.getPrice());
        System.out.println("\nCalculated final total (including discounts): Rs." + finalTotal);

        //Log the checkout transaction
        LoggerUtil.logAction("CHECKOUT_COMPLETE","SYSTEM", cart.size());
        System.out.println("\nTransaction recorded in audit_log.txt successfully");


        }
    }

