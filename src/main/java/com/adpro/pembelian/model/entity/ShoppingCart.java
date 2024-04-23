package com.adpro.pembelian.model.entity;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
public class ShoppingCart {
    @Id
    private Long userId;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "productId")
    private Map<String, CartItem> cartItemMap;

    @Transient
    private PricingStrategy<CartItem> pricingStrategy;
    public ShoppingCart(){
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
