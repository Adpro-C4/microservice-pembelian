package com.adpro.pembelian.model.builder;

import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;

import java.util.Map;

public  class ShoppingCartBuilder {
    private Long userId;
    private Map<String, CartItemEntity> cartItemMap;

    public ShoppingCartBuilder() {
    }

    public ShoppingCartBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ShoppingCartBuilder withCartItems(Map<String, CartItemEntity> cartItemMap){
        this.cartItemMap = cartItemMap;
        return  this;
    }

    public ShoppingCartEntity build() {
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setUserId(userId);
        shoppingCart.setCartItemMap(cartItemMap);
        return shoppingCart;
    }
}