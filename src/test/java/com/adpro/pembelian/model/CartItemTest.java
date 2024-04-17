package com.adpro.pembelian.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {
    @Test
    public void testCalculateTotalPrice() {

        CartItem cartItem = new CartItem();
        cartItem.setProductId("123");
        cartItem.setQuantity(3);
        cartItem.setPrice(2000);
        double totalPrice = cartItem.calculateTotalPrice();
        assertEquals(6000, totalPrice);
    }

    @Test
    public void testSetNegativeQuantity() {
        CartItem cartItem = new CartItem();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(-3);
        });
    }

    @Test
    public void testSetZeroQuantity() {
        CartItem cartItem = new CartItem();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(0);
        });
    }
}
