package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.entity.CartItem;

import java.util.List;

public class CartPricingStrategy implements PricingStrategy<CartItem> {
    @Override
    public double calculateTotalPrice(List<CartItem> items) {
        double totalPrice = 0;
        for(CartItem item : items ){
            totalPrice+= item.calculateTotalPrice();
        }
        return totalPrice;
    }
}
