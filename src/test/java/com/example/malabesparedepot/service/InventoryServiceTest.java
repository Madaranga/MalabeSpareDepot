package com.example.malabesparedepot.service;

import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    private final InventoryService service = new InventoryService();
    private Part p(String code, int qty) { return new Part(code,code,"B",10,qty,"ENGINE","",""); }

    @Test void detectsStockAtAndBelowThresholdIncludingZero() {
        List<Part> result = service.findLowStock(List.of(p("A",0),p("B",5),p("C",6)),5);
        assertEquals(List.of("A","B"), result.stream().map(Part::getPartCode).toList());
    }
    @Test void emptyInventoryProducesNoLowStockItems() { assertTrue(service.findLowStock(List.of(),5).isEmpty()); }
    @Test void rejectsNegativeThreshold() { assertThrows(IllegalArgumentException.class,()->service.findLowStock(List.of(),-1)); }
    @Test void deductsStockExactly() { Part part=p("A",10); service.deductStock(part,4); assertEquals(6,part.getQuantity()); }
    @Test void allowsDeductionToZero() { Part part=p("A",2); service.deductStock(part,2); assertTrue(service.isOutOfStock(part)); }
    @Test void preventsNegativeStock() { Part part=p("A",2); assertThrows(IllegalArgumentException.class,()->service.deductStock(part,3)); assertEquals(2,part.getQuantity()); }
    @Test void rejectsZeroNegativeAndNullDeductionInput() {
        Part part=p("A",2);
        assertThrows(IllegalArgumentException.class,()->service.deductStock(part,0));
        assertThrows(IllegalArgumentException.class,()->service.deductStock(part,-1));
        assertThrows(NullPointerException.class,()->service.deductStock(null,1));
    }
}
