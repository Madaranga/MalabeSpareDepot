package com.example.malabesparedepot;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.util.DataParser;
import com.example.malabesparedepot.util.DealerSelector;
import com.example.malabesparedepot.util.CustomSorter;
import com.example.malabesparedepot.util.SearchEngine;
import java.util.ArrayList;
import java.util.List;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("Testing custom algorithm logic");

        List<Part> inventory = DataParser.parseInventory("inventory_legacy.txt");
        List<Dealer> dealers = DataParser.parseDealers("dealers_legacy.txt");

        System.out.println("--- Testing manual sort ---");
        CustomSorter.sortInventory(inventory);
        for (Part p : inventory) {
            System.out.println(p.getCategory() + " -- " + p.getPartCode() + " -- " + p.getName());
        }
    }
}
