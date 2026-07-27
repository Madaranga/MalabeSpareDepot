package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public static List<Part> parseInventory(String filePath) {
        List<Part> partList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                try {
                    String[] tokens = splitAndTrim(line);
                    if (tokens.length < 6) {
                        System.err.println("Skipping inventory line " + lineNumber
                                + ": expected at least 6 fields.");
                        continue;
                    }
                    String partCode = tokens[0];
                    String name = tokens[1];
                    String brand = tokens[2].isEmpty() ? "Unknown" : tokens[2];

                    String cleanPrice = tokens[3].replace("Rs.", "").trim();
                    String priceStr = cleanPrice.replaceAll("[^0-9.]", "");
                    double price = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);
                    int quantity = Integer.parseInt(tokens[4]);
                    String category = tokens[5].toUpperCase();
                    String dateStr = tokens.length > 6 ? tokens[6] : "Unknown";
                    String imagePath = tokens.length > 7 ? tokens[7] : "";

                    partList.add(new Part(partCode, name, brand, price, quantity,
                            category, dateStr, imagePath));
                } catch (NumberFormatException exception) {
                    System.err.println("Skipping malformed inventory line " + lineNumber
                            + ": " + exception.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error parsing inventory: " + e.getMessage());
        }
        return partList;
    }


    public static List<Dealer> parseDealers(String filePath){
        List<Dealer> dealerList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] tokens = splitAndTrim(line);
                if (tokens.length < 3) {
                    System.err.println("Skipping dealer line " + lineNumber
                            + ": expected at least 3 fields.");
                    continue;
                }

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

    private static String[] splitAndTrim(String line) {
        String[] tokens = line.split("[,;|]");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        return tokens;
    }
}
