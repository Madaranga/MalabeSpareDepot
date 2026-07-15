package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import java.util.List;

public class CustomSorter {

    // Main method to sort inventory parts
    public static void sortInventory(List<Part> parts) {
        int n = parts.size();
        for (int i = 1; i < n; i++) {
            Part key = parts.get(i);
            int j = i - 1;

            // Shift items that are larger than the key element
            while (j >= 0 && shouldSwapParts(parts.get(j), key)) {
                parts.set(j + 1, parts.get(j));
                j =  j - 1;
            }
            parts.set(j + 1, key);
        }
    }

    // Secondary sorting rules for parts
    private static boolean shouldSwapParts(Part p1, Part p2) {
        int categoryCompare = p1.getCategory().compareTo(p2.getCategory());
        if (categoryCompare > 0) {
            return true;
        }
        else if (categoryCompare == 0) {
            return p1.getPartCode().compareTo(p2.getPartCode()) > 0;
        }
        return false;
    }

    // Dealers by alphabetically by location
    public static void sortDealersByLocation(List<Dealer> dealers) {
        int n = dealers.size();
        for (int i = 1; i < n; i++) {
            Dealer key = dealers.get(i);
            int j = i - 1;

            while (j >= 0 && dealers.get(j).getLocation().compareTo(key.getLocation()) > 0) {
                dealers.set(j + 1, dealers.get(j));
                j =  j - 1;
            }
            dealers.set(j + 1, key);
        }
    }


}
