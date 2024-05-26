package com.adpro.pembelian.model;
import org.junit.jupiter.api.Test;

import com.adpro.pembelian.model.dto.DTOCartItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DTOCartItemTest {

    @Test
    void testDTOCartItem() {
        // Test data
        Long id = 1L;
        String productId = "ABC123";
        String name = "Product Name";
        int quantity = 3;
        double price = 10.99;

        // Create DTOCartItem objects
        DTOCartItem dtoCartItem1 = new DTOCartItem();
        dtoCartItem1.setId(id);
        dtoCartItem1.setProductId(productId);
        dtoCartItem1.setName(name);
        dtoCartItem1.setQuantity(quantity);
        dtoCartItem1.setPrice(price);

        DTOCartItem dtoCartItem2 = new DTOCartItem();
        dtoCartItem2.setId(id);
        dtoCartItem2.setProductId(productId);
        dtoCartItem2.setName(name);
        dtoCartItem2.setQuantity(quantity);
        dtoCartItem2.setPrice(price);

        DTOCartItem dtoCartItem3 = new DTOCartItem();
        dtoCartItem3.setId(id);
        dtoCartItem3.setName("bukan");
        dtoCartItem3.setProductId(productId);
        dtoCartItem3.setQuantity(quantity);
        dtoCartItem3.setPrice(price);

        // Test toString method
        String expectedToString = "DTOCartItem{id=1, productId='ABC123', name='Product Name', quantity=3, price=10.99}";
        assertEquals(expectedToString, dtoCartItem1.toString());

        // Test equals method
        assertEquals(dtoCartItem1, dtoCartItem2);
        assertNotEquals(null, dtoCartItem1); // Non-nullity
        assertNotEquals(dtoCartItem1, new Object()); // Comparison to an object of a different class

        // Test hashCode method
        assertEquals(dtoCartItem1.hashCode(), dtoCartItem2.hashCode());
        assertEquals(false, dtoCartItem1.equals(dtoCartItem3));
        // Test canEqual method
        assertEquals(true, dtoCartItem1.equals(dtoCartItem2));
    }
}

