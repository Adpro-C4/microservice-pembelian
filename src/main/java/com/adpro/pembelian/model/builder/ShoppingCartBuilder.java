package com.adpro.pembelian.model.builder;

import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.model.entity.ShoppingCart;

import java.util.Map;

public  class ShoppingCartBuilder {
    private Long userId;
    private Map<String, CartItem> cartItemMap;

    public ShoppingCartBuilder() {
    }

    public ShoppingCartBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ShoppingCartBuilder withCartItems(Map<String, CartItem> cartItemMap){
        this.cartItemMap = cartItemMap;
        return  this;
    }

    public ShoppingCart build() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setCartItemMap(cartItemMap);
        return shoppingCart;
    }
}