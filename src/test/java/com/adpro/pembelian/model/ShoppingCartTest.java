package com.adpro.pembelian.model;

import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.model.entity.ShoppingCart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    @Test
    void testCreateShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        assertNotNull(shoppingCart);
        assertTrue(shoppingCart.getCartItemMap().isEmpty());
    }

    @Test
    void testGetEmptyShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        assertTrue(shoppingCart.getCartItemMap().isEmpty());
    }

    @Test
    void testAddCartItemSuccess() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);
        assertFalse(shoppingCart.getCartItemMap().isEmpty());
        assertTrue(shoppingCart.getCartItemMap().containsKey("PROD123"));
        assertEquals(cartItem, shoppingCart.getCartItemMap().get("PROD123"));
    }

    @Test
    void testAddCartItemFailed() {
        ShoppingCart shoppingCart = new ShoppingCart();
        assertThrows(NullPointerException.class, () -> shoppingCart.addItem(null));
    }

    @Test
    void testRemoveCartItemSuccess() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);
        assertTrue(shoppingCart.getCartItemMap().containsKey("PROD123"));

        shoppingCart.deleteItem(cartItem);
        assertFalse(shoppingCart.getCartItemMap().containsKey("PROD123"));
    }

    @Test
    void testRemoveCartItemFailed() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem(1L, "PROD123", "Product Name", 1, 10.0);
        assertDoesNotThrow(() -> shoppingCart.deleteItem(cartItem)); // Attempt to remove non-existent item should not throw exception
    }

    @Test
    void testUpdateCartItemSuccess() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);

        CartItem updatedCartItem = new CartItem(1L, "PROD123", "Updated Product Name", 2, 15.0);
        shoppingCart.addItem(updatedCartItem);

        assertEquals(updatedCartItem, shoppingCart.getCartItemMap().get("PROD123"));
    }

    @Test
    void testUpdateCartItemFailed() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem(1L, "PROD123", "Product Name", 1, 10.0);
        assertThrows(NullPointerException.class, () -> shoppingCart.addItem(null));
    }
}

