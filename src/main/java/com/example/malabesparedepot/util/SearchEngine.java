package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Part;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {

    public static List<Part> filterInventory(List<Part> originalList, String category, double minPrice, double maxPrice, String keyword) {
        List<Part> filteredResult = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();

        for (Part part : originalList) {
            //
            if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("ALL")) {
                if (!part.getCategory().equalsIgnoreCase(category)) {
                    continue;
                }
            }

            //
            if (part.getPrice() < minPrice || part.getPrice() > maxPrice) {
                continue;
            }

            //
            if (!lowerKeyword.isEmpty()) {
                boolean matchesName = part.getName().toLowerCase().contains(lowerKeyword);
                boolean matchesBrand = part.getBrand().toLowerCase().contains(lowerKeyword);
                if (!matchesName && !matchesBrand) {
                    continue;
                }
            }
            filteredResult.add(part);
        }
        return filteredResult;
    }



}
