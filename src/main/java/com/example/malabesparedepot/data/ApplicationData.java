package com.example.malabesparedepot.data;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.util.DataParser;
import com.example.malabesparedepot.util.DealerSelector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ApplicationData {
    private static final ApplicationData INSTANCE = new ApplicationData();

    private final ObservableList<Part> parts;
    private final ObservableList<Dealer> dealers;
    private final ObservableList<Dealer> saleDealers;
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    private ApplicationData(){
        parts = FXCollections.observableArrayList(DataParser.parseInventory(DataParser.DEFAULT_INVENTORY_FILE));
        dealers = FXCollections.observableArrayList(DataParser.parseDealers(DataParser.DEFAULT_DEALERS_FILE));
        List<Dealer> selectedDealers = DealerSelector.getRandomFoundDealers(dealers);
        saleDealers = FXCollections.observableArrayList(selectedDealers);
    }

    public static ApplicationData getInstance(){
        return INSTANCE;
    }
    public ObservableList<Part> getParts(){
        return parts;
    }
    public ObservableList<Dealer> getDealers(){
        return dealers;
    }
    public ObservableList<Dealer> getSaleDealers(){
        return saleDealers;
    }
    public ObservableList<CartItem> getCartItems(){
        return cartItems;
    }
    public  ObservableList<String> getCategories(){
        Set<String> categories = new LinkedHashSet<>();
        for(Part part : parts){
            categories.add(part.getCategory());
        }
        return FXCollections.observableArrayList(categories);
    }

    public Part findPartByCode(String code) {
        if (code == null) {
            return null;
        }
        for (Part part : parts) {
            if (part.getPartCode().equalsIgnoreCase(code.trim())) {
                return part;
            }
        }
        return null;
    }

    public void touchPart(Part part) {
        for (CartItem item : cartItems) {
            if (item.getPart() == part) {
                item.setQuantity(item.getQuantity());
                cartItems.set(cartItems.indexOf(item), item);
                break;
            }
        }
        int index = parts.indexOf(part);
        if (index >= 0) {
            parts.set(index, part);
        }
    }

    public void removePart(Part part) {
        cartItems.removeIf(item -> item.getPart() == part);
        parts.remove(part);
    }


}
