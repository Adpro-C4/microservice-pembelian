package com.adpro.pembelian.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    public void testBuildCartItem() {

        Long id = 1L;
        String productId = "ABC123";
        String name = "Product Name";
        int quantity = 2;
        double price = 10.99;

        CartItem cartItem = new CartItemBuilder()
                .withId(id)
                .withProductId(productId)
                .withName(name)
                .withQuantity(quantity)
                .withPrice(price)
                .build();

        assertEquals(id, cartItem.getId());
        assertEquals(productId, cartItem.getProductId());
        assertEquals(name, cartItem.getName());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(price, cartItem.getPrice(), 0.001);
    }


    @Test
    public void testSetNegativePrice(){
        CartItem cartItem = new CartItem();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setPrice(-3);
        });
    }

    @Test
    public void testSetPositivePrice(){
        CartItem cartItem = new CartItem();
        cartItem.setPrice(2000);
        assertEquals(2000, cartItem.getPrice() );
    }

    @Test
    public  void testZeroPrice(){
        CartItem cartItem = new CartItem();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(0);
        });
    }

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
