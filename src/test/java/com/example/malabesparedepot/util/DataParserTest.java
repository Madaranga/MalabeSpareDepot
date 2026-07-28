package com.example.malabesparedepot.util;

import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.model.Part;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataParserTest {
    @TempDir Path tempDir;

    private Path file(String name, String content) throws Exception {
        Path path = tempDir.resolve(name);
        Files.writeString(path, content);
        return path;
    }

    @Test void parsesMixedDelimitersWhitespaceCurrencyAndOptionalFields() throws Exception {
        Path path = file("inventory.txt", "P1, Plug, NGK, Rs. 850.50, 4, electrical, 2026-01-01, plug.png\n" +
                "P2|Brake Pad||1,250|8|Brakes\n" +
                "P3 ; Tyre ; Local ; £6500 ; 2 ; Bodywork ; ; tyre.png\n");
        List<Part> parts = DataParser.parseInventory(path.toString());
        assertEquals(3, parts.size());
        assertEquals(850.50, parts.get(0).getPrice(), 0.001);
        assertEquals("BRAKES", parts.get(1).getDateAdded().toUpperCase());
        assertEquals("Unknown", parts.get(1).getBrand());
        assertEquals("ELECTRICAL", parts.get(0).getCategory());
    }

    @Test void skipsBlankMissingAndMalformedInventoryLines() throws Exception {
        Path path = file("bad.txt", "\nP1,Only,Five,10,2\nP2,Plug,NGK,wrong,abc,Electrical\nP3,Valid,Brand,100,1,Engine\n");
        List<Part> parts = DataParser.parseInventory(path.toString());
        assertEquals(1, parts.size());
        assertEquals("P3", parts.getFirst().getPartCode());
    }

    @Test void invalidPriceSymbolsBecomeZeroWhenNoDigitsExist() throws Exception {
        Path path = file("price.txt", "P1,Plug,NGK,not-priced,2,Electrical\n");
        assertEquals(0.0, DataParser.parseInventory(path.toString()).getFirst().getPrice(), 0.001);
    }

    @Test void missingFileReturnsEmptyInventoryList() {
        assertTrue(DataParser.parseInventory(tempDir.resolve("missing.txt").toString()).isEmpty());
    }

    @Test void parsesDealersWithMissingLocationAndMixedDelimiters() throws Exception {
        Path path = file("dealers.txt", "D1, Alpha, 071, Colombo\nD2|Beta|072\nD3 ; Gamma ; 073 ; Kandy\n");
        List<Dealer> dealers = DataParser.parseDealers(path.toString());
        assertEquals(3, dealers.size());
        assertEquals("Unknown", dealers.get(1).getLocation());
    }

    @Test void skipsDealerLinesWithMissingRequiredFieldsAndBlankLines() throws Exception {
        Path path = file("dealers-bad.txt", "\nD1,OnlyName\nD2,Valid,071,Malabe\n");
        List<Dealer> dealers = DataParser.parseDealers(path.toString());
        assertEquals(1, dealers.size());
        assertEquals("D2", dealers.getFirst().getDealerId());
    }
}
