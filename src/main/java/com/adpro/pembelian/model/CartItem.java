package com.adpro.pembelian.model;
import com.adpro.pembelian.service.PricingStrategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private PricingStrategy<CartItem> pricingStrategy;

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public void validateQuantity(int quantity){
        if(quantity <=0) throw new IllegalArgumentException("Cart Item tidak boleh kosong");
    }



    public double calculateTotalPrice() {
        return pricingStrategy.calculateTotalPrice(this);
    }
}

