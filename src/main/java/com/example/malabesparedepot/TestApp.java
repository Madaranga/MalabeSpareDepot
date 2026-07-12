package com.example.malabesparedepot;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.util.DataParser;

import java.util.ArrayList;
import java.util.List;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("Testing legacy data phasing");

        List<Part> inventory = DataParser.parseInventory("inventory_legacy.txt");
        List<Dealer> dealers = DataParser.parseDealers("dealers_legacy.txt");

        System.out.println("Total parts extracted: " + inventory.size());
        System.out.println("Total dealers extracted: " + dealers.size());

        if (!inventory.isEmpty()) {
            Part firstItem = inventory.get(0);
            System.out.println("\nVerification test");
            System.out.println("Part Code: " + firstItem.getPartCode());
            System.out.println("Cleaned Price:" + firstItem.getPrice());
            System.out.println("Normalized Category:" + firstItem.getCategory());
        }
    }
}
