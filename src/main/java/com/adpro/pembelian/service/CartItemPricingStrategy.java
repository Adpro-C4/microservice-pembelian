package com.adpro.pembelian.service;

import com.adpro.pembelian.model.CartItem;

public class CartItemPricingStrategy implements PricingStrategy<CartItem>{

    @Override
    public double calculateTotalPrice(CartItem item) {
        return 0;
    }
}
