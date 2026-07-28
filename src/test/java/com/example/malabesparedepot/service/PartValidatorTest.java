package com.example.malabesparedepot.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PartValidatorTest {
    private final PartValidator validator = new PartValidator();
    @Test void acceptsValidAndBoundaryValues() { assertDoesNotThrow(()->validator.validate("P1","Plug","NGK",0,0,"ENGINE")); }
    @Test void rejectsNullEmptyAndBlankRequiredFields() {
        assertThrows(IllegalArgumentException.class,()->validator.validate(null,"N","B",1,1,"C"));
        assertThrows(IllegalArgumentException.class,()->validator.validate("","N","B",1,1,"C"));
        assertThrows(IllegalArgumentException.class,()->validator.validate("P"," ","B",1,1,"C"));
    }
    @Test void rejectsNegativeNonFinitePriceAndNegativeQuantity() {
        assertThrows(IllegalArgumentException.class,()->validator.validate("P","N","B",-0.01,1,"C"));
        assertThrows(IllegalArgumentException.class,()->validator.validate("P","N","B",Double.NaN,1,"C"));
        assertThrows(IllegalArgumentException.class,()->validator.validate("P","N","B",1,-1,"C"));
    }
}
