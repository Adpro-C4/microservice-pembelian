package com.adpro.pembelian.model.builder;

import com.adpro.pembelian.model.entity.CartItemEntity;

public class CartItemBuilder {
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private double price;

    public CartItemBuilder withId(Long id) {
        this.id = id;
        return  this;
    }

    public CartItemBuilder withName(String name) {
        this.name = name;
        return  this;
    }

    public CartItemBuilder withPrice(double price) {
        this.price = price;
        return  this;
    }

    public CartItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public CartItemBuilder withProductId(String productId) {
        this.productId = productId;
        return  this;
    }

    public CartItemEntity build(){
        return new CartItemEntity(this.id,this.productId,this.name,this.quantity,this.price);
    }
}
