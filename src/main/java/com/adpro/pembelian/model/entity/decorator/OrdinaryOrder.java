package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrdinaryOrder extends Order {
    private String userId;
    private  String timestamp;
    private DTOCustomerDetails customerDetails;
    private String address;
    private List<CartItem> cartItems;
    private PricingStrategy<CartItem> strategy;
    private String price;

    public OrdinaryOrder(){
        strategy = new CartPricingStrategy();
    }



    @Override
    public double getTotalPrice() {
        return strategy.calculateTotalPrice(this.cartItems);
    }
}

