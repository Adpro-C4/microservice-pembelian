package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.entity.CartItemEntity;

import java.util.List;

public class CartPricingStrategy implements PricingStrategy<CartItemEntity> {
    @Override
    public double calculateTotalPrice(List<CartItemEntity> items) {
        double totalPrice = 0;
        for(CartItemEntity item : items ){
            totalPrice+= item.calculateTotalPrice();
        }
        return totalPrice;
    }
}
