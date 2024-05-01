package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.entity.OrderTemplate;
import jakarta.persistence.*;



public abstract class OrderDecorator extends OrderTemplate {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    protected OrderTemplate request;

    public OrderDecorator(OrderTemplate request){

        this.request = request;
    }

    public OrderDecorator() {

    }
}
