package com.adpro.pembelian.model;

import com.adpro.pembelian.service.CartPricingStrategy;
import com.adpro.pembelian.service.PricingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrdinaryPurchaseRequest implements PurchaseRequest{
    private String id;
    private  String timestamp;
    private CustomerDetails customerDetails;
    private String address;
    private List<CartItem> cartItems;
    private PricingStrategy<CartItem> strategy;

    public  OrdinaryPurchaseRequest(){
        strategy = new CartPricingStrategy();
    }

    @Override
    public double getTotalPrice() {
        return strategy.calculateTotalPrice(this.cartItems);
    }
}

