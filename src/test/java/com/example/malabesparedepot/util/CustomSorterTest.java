package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CustomSorterTest {
    private Part p(String code, String category) { return new Part(code, code, "B", 1, 1, category, "", ""); }
    @Test void sortsInventoryByCategoryThenPartCode() {
        List<Part> list = new ArrayList<>(List.of(p("P2","ENGINE"), p("P3","BRAKES"), p("P1","ENGINE")));
        CustomSorter.sortInventory(list);
        assertEquals(List.of("P3","P1","P2"), list.stream().map(Part::getPartCode).toList());
    }
    @Test void handlesEmptySingleAlreadySortedAndReverseSortedLists() {
        List<Part> empty = new ArrayList<>(); CustomSorter.sortInventory(empty); assertTrue(empty.isEmpty());
        List<Part> one = new ArrayList<>(List.of(p("P1","A"))); CustomSorter.sortInventory(one); assertEquals("P1",one.getFirst().getPartCode());
        List<Part> sorted = new ArrayList<>(List.of(p("P1","A"),p("P2","B"))); CustomSorter.sortInventory(sorted); assertEquals("P1",sorted.getFirst().getPartCode());
        List<Part> reverse = new ArrayList<>(List.of(p("P2","B"),p("P1","A"))); CustomSorter.sortInventory(reverse); assertEquals("P1",reverse.getFirst().getPartCode());
    }
    @Test void preservesDuplicateSortKeys() {
        Part a = p("P1","A"), b = p("P1","A"); List<Part> list = new ArrayList<>(List.of(a,b));
        CustomSorter.sortInventory(list); assertSame(a,list.get(0)); assertSame(b,list.get(1));
    }
    @Test void sortsDealersByLocation() {
        List<Dealer> dealers = new ArrayList<>(List.of(new Dealer("1","A","","Kandy"),new Dealer("2","B","","Colombo")));
        CustomSorter.sortDealersByLocation(dealers); assertEquals("Colombo",dealers.getFirst().getLocation());
    }
}
