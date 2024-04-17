package com.adpro.pembelian.model;
import com.adpro.pembelian.service.PricingStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartItemTest {
    @Test
    public void testCalculateTotalPrice() {

        CartItem cartItem = new CartItem();
        cartItem.setProductId("123");
        cartItem.setQuantity(3);

        PricingStrategy<CartItem> pricingStrategy = mock(PricingStrategy.class);

        when(pricingStrategy.calculateTotalPrice(cartItem)).thenReturn(30.0);

        cartItem.setPricingStrategy(pricingStrategy);

        double totalPrice = cartItem.calculateTotalPrice();

        assertEquals(30.0, totalPrice);
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
