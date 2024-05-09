package com.adpro.pembelian.model.entity;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
public class ShoppingCartEntity {
    @Id
    private Long userId;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @MapKey(name = "productId")
    private Map<String, CartItemEntity> cartItemMap;

    @Transient
    private PricingStrategy<CartItemEntity> pricingStrategy;
    public ShoppingCartEntity(){
        this.cartItemMap = new HashMap<>();
        this.pricingStrategy = new CartPricingStrategy();
    }

    public CartItemEntity addItem(CartItemEntity cartItem){
        cartItemMap.put(cartItem.getProductId(), cartItem);
        return cartItem;
    }
    public void deleteItem(CartItemEntity cartItem){
        cartItemMap.remove(cartItem.getProductId());
    }
    public double calculateTotalPrice(){
        return this.pricingStrategy.calculateTotalPrice(new ArrayList<>(cartItemMap.values()));
    }
}
