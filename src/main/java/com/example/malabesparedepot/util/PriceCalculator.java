package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.CartItem;
import java.util.List;

public class PriceCalculator {

    public static double calculateFinalTotal(List<CartItem> cart) {
        double runningTotal = 0.0;
        boolean hasEngine = false;
        boolean hasElectrical = false;

        //Calculate item subtotals
        for (CartItem item: cart) {
            double itemSubtotal = item.getTotalprice();

            if (item.getQuantity() >= 3) {
                itemSubtotal = itemSubtotal * 0.95; // 5% discount
            }
            runningTotal += itemSubtotal;


            //Track categories
            String category = item.getPart().getCategory().toUpperCase();
            if (category.contains("Electrical")) {
                hasElectrical = true;
            }
            if (category.contains("Engine")) {
                hasEngine = true;
            }
        }

        //10% discount
        if (hasEngine && hasElectrical) {
            runningTotal = runningTotal*0.90;
        }

        return runningTotal;




    }
}
