package com.adpro.pembelian.model;

import com.adpro.pembelian.service.CartPricingStrategy;
import com.adpro.pembelian.service.PricingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ShoppingCart {
    private Long userId;
    private Map<String, CartItem> cartItemMap;
    private PricingStrategy<CartItem> pricingStrategy;
    ShoppingCart(){
        this.cartItemMap = new HashMap<>();
        this.pricingStrategy = new CartPricingStrategy();
    }
    public CartItem addItem(CartItem cartItem){
        cartItemMap.put(cartItem.getProductId(), cartItem);
        return cartItem;
    }
    public void deleteItem(CartItem cartItem){
        cartItemMap.remove(cartItem.getProductId());
    }
    public double calculateTotalPrice(){
        return this.pricingStrategy.calculateTotalPrice(new ArrayList<>(cartItemMap.values()));
    }
}
