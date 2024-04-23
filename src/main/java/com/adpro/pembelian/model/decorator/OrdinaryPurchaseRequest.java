package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOPurchaseRequest;
import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrdinaryPurchaseRequest implements DTOPurchaseRequest {
    private String id;
    private  String timestamp;
    private DTOCustomerDetails customerDetails;
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

