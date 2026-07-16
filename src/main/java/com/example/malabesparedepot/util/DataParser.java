package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public static List<Part> parseInventory(String filePath) {
        List<Part> partList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                //Split
                String[] tokens = line.split("[,;|]");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }

                if (tokens.length < 6) continue;

                String partCode = tokens[0];
                String name = tokens[1];
                String brand = tokens[2].isEmpty() ? "Unknown" : tokens[2];

                //Remove "Rs.", spaces, ...
                String cleanPrice = tokens[3].replace("Rs.","").trim();
                String priceStr = cleanPrice.replaceAll("[^0-9.]","");
                double price = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);

                int quantity = Integer.parseInt(tokens[4]);
                String category = tokens[5].toUpperCase();
                String dateStr = tokens.length > 6 ? tokens[6] : "Unknown";
                String imagePath = tokens.length > 7 ? tokens[7] : "default.png";

                partList.add(new Part(partCode, name, brand, price, quantity, category, dateStr, imagePath));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing inventory: " + e.getMessage());
        }
        return partList;
    }


    public static List<Dealer> parseDealers(String filePath){
        List<Dealer> dealerList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split("[,;|]");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }

                if (tokens.length < 3) continue;

                String dealerId = tokens[0];
                String name = tokens[1];
                String phoneNumber = tokens[2];
                String location = tokens.length > 3 ? tokens[3] : "Unknown";

                dealerList.add(new Dealer(dealerId, name, phoneNumber, location));
            }

        } catch (IOException e) {
            System.err.println("Error parsing dealers: " + e.getMessage());
        }
        return dealerList;
    }

}
