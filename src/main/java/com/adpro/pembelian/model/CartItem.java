package com.adpro.pembelian.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private double price;

    public CartItem(){}
    public CartItem(Long id, String productId, String name, int quantity, double price){
        this.id = id;
        this.productId = productId;
        this.name = name;
        setPrice(price);
        setQuantity(quantity);
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public void validatePrice(double price){
        if(price<=0) throw  new IllegalArgumentException("harga harus bilangan positif");
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }
    public void validateQuantity(int quantity){
        if(quantity <=0) throw new IllegalArgumentException("Cart Item tidak boleh kosong");
    }
    public double calculateTotalPrice() {
        return price * quantity;
    }
}

