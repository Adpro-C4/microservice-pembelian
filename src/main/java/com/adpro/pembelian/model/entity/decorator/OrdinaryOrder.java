package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.service.internal.PricingStrategy;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class OrdinaryOrder extends Order {
    public OrdinaryOrder(){
        strategy = new CartPricingStrategy();
    }
    @Override
    public double getTotalPrice() {
        return strategy.calculateTotalPrice(this.cartItems);
    }
}

