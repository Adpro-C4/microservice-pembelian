package com.adpro.pembelian.model;

import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class ShoppingCartTest {
    private ShoppingCartEntity shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCartEntity();
        shoppingCart.setUserId(123L);
    }

    @Test
    void testSetPricingStrategy() {
        PricingStrategy<CartItemEntity> newStrategy = items -> items.stream().mapToDouble(CartItemEntity::calculateTotalPrice).sum();
        shoppingCart.setPricingStrategy(newStrategy);

        assertEquals(newStrategy, shoppingCart.getPricingStrategy());
        assertNotNull(shoppingCart.getPricingStrategy());
    }

    @Test
    void testGetUserId() {
        assertEquals(123L, shoppingCart.getUserId());
    }

    @Test
    void testGetPricingStrategy() {
        PricingStrategy<CartItemEntity> defaultStrategy = new CartPricingStrategy();
        assertEquals(defaultStrategy.getClass(), shoppingCart.getPricingStrategy().getClass());
    }

    @Test
    void testCreateShoppingCart() {
        assertNotNull(shoppingCart);
        assertTrue(shoppingCart.getCartItemMap().isEmpty());
    }

    @Test
    void testGetEmptyShoppingCart() {
        assertTrue(shoppingCart.getCartItemMap().isEmpty());
    }

    @Test
    void testAddCartItemSuccess() {
        CartItemEntity cartItem = new CartItemEntity(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);
        assertFalse(shoppingCart.getCartItemMap().isEmpty());
        assertTrue(shoppingCart.getCartItemMap().containsKey("PROD123"));
        assertEquals(cartItem, shoppingCart.getCartItemMap().get("PROD123"));
    }

    @Test
    void testAddCartItemFailed() {
        assertThrows(NullPointerException.class, () -> shoppingCart.addItem(null));
    }

    @Test
    void testRemoveCartItemSuccess() {
        CartItemEntity cartItem = new CartItemEntity(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);
        assertTrue(shoppingCart.getCartItemMap().containsKey("PROD123"));

        shoppingCart.deleteItem(cartItem);
        assertFalse(shoppingCart.getCartItemMap().containsKey("PROD123"));
    }

    @Test
    void testRemoveCartItemFailed() {
        CartItemEntity cartItem = new CartItemEntity(1L, "PROD123", "Product Name", 5, 10.0);
        assertDoesNotThrow(() -> shoppingCart.deleteItem(cartItem)); // Attempt to remove non-existent item should not throw exception
    }

    @Test
    void testUpdateCartItemSuccess() {
        CartItemEntity cartItem = new CartItemEntity(1L, "PROD123", "Product Name", 1, 10.0);
        shoppingCart.addItem(cartItem);

        CartItemEntity updatedCartItem = new CartItemEntity(1L, "PROD123", "Updated Product Name", 2, 15.0);
        shoppingCart.addItem(updatedCartItem);

        assertEquals(updatedCartItem, shoppingCart.getCartItemMap().get("PROD123"));
    }

    @Test
    void testUpdateCartItemFailed() {
        assertThrows(NullPointerException.class, () -> shoppingCart.addItem(null));
    }

    
}

