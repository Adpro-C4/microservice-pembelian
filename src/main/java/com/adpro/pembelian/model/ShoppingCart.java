package com.adpro.pembelian.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ShoppingCart {
    private Long userId;
    private Map<String, CartItem> cartItemMap;
    ShoppingCart(){
        this.cartItemMap = new HashMap<>();
    }
    public CartItem addItem(CartItem item){
        cartItemMap.put(item.getProductId(), item);
        return item;
    }
    public void deleteItem(CartItem item){
        cartItemMap.remove(item.getProductId());
    }
}
