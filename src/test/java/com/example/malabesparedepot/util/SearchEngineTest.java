package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SearchEngineTest {
    private List<Part> parts;
    @BeforeEach void setUp() {
        parts = List.of(
                new Part("P1", "Spark Plug", "NGK", 850, 5, "ELECTRICAL", "", ""),
                new Part("P2", "Brake Pad", "Bajaj", 1250, 8, "BRAKES", "", ""),
                new Part("P3", "Ignition Coil", "Bajaj", 2200, 2, "ELECTRICAL", "", ""));
    }
    @Test void filtersByExactCategoryCaseInsensitively() { assertEquals(2, SearchEngine.filterInventory(parts,"electrical",0,9999,"").size()); }
    @Test void findsPartialKeywordInNameOrBrandCaseInsensitively() { assertEquals(2, SearchEngine.filterInventory(parts,"ALL",0,9999,"BAJ").size()); }
    @Test void appliesInclusivePriceBoundaries() { assertEquals(List.of(parts.get(1)), SearchEngine.filterInventory(parts,"ALL",1250,1250,"")); }
    @Test void returnsAllForEmptyOrNullKeywordAndAllCategory() { assertEquals(3, SearchEngine.filterInventory(parts,"ALL",0,9999,null).size()); }
    @Test void returnsEmptyWhenNothingMatches() { assertTrue(SearchEngine.filterInventory(parts,"ENGINE",0,100,"xyz").isEmpty()); }
    @Test void supportsEmptyInputList() { assertTrue(SearchEngine.filterInventory(List.of(),"ALL",0,1000,"").isEmpty()); }
    @Test void rejectsNullInputList() { assertThrows(IllegalArgumentException.class, () -> SearchEngine.filterInventory(null,"ALL",0,1000,"")); }
}
