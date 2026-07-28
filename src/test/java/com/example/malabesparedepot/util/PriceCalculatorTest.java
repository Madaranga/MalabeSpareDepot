package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {
    private CartItem item(String category, double price, int quantity) {
        return new CartItem(new Part("P"+category, category, "B", price, 100, category, "", ""), quantity);
    }
    @Test void emptyCartTotalIsZero() { assertEquals(0, PriceCalculator.calculateFinalTotal(List.of()), 0.001); }
    @Test void noDiscountBelowThreeUnits() { assertEquals(200, PriceCalculator.calculateFinalTotal(List.of(item("BRAKES",100,2))),0.001); }
    @Test void appliesFivePercentDiscountAtThreeUnits() { assertEquals(285, PriceCalculator.calculateFinalTotal(List.of(item("BRAKES",100,3))),0.001); }
    @Test void appliesFivePercentDiscountAboveBoundary() { assertEquals(380, PriceCalculator.calculateFinalTotal(List.of(item("BRAKES",100,4))),0.001); }
    @Test void appliesTenPercentCombinedCategoryDiscount() {
        double total = PriceCalculator.calculateFinalTotal(List.of(item("ENGINE",100,1),item("ELECTRICAL",200,1)));
        assertEquals(270,total,0.001);
    }
    @Test void appliesQuantityDiscountBeforeCombinedDiscount() {
        double total = PriceCalculator.calculateFinalTotal(List.of(item("ENGINE",100,3),item("ELECTRICAL",200,1)));
        assertEquals((285+200)*0.90,total,0.001);
    }
    @Test void categoryMatchingIsCaseInsensitive() {
        assertEquals(270, PriceCalculator.calculateFinalTotal(List.of(item("engine",100,1),item("electrical",200,1))),0.001);
    }
}
