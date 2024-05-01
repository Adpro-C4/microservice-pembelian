package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.service.internal.CartPricingStrategy;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class OrdinaryOrderEntity extends OrderTemplate {
    public OrdinaryOrderEntity(){
        strategy = new CartPricingStrategy();
    }
    @Override
    public double getTotalPrice() {
        return strategy.calculateTotalPrice(this.cartItems);
    }
}

