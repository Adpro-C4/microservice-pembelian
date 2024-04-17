package com.adpro.pembelian.service;

import com.adpro.pembelian.model.CartItem;

import java.util.List;

public class CartPricingStrategy implements PricingStrategy<CartItem>{
    @Override
    public double calculateTotalPrice(List<CartItem> items) {
        double totalPrice = 0;
        for(CartItem item : items ){
            totalPrice+= item.calculateTotalPrice();
        }
        return totalPrice;
    }
}
