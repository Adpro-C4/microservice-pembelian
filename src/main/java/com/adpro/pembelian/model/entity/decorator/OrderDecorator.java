package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.entity.Order;
import jakarta.persistence.*;


@Entity
public abstract class OrderDecorator extends Order {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    protected Order request;

    public OrderDecorator(Order request){

        this.request = request;
    }

    public OrderDecorator() {

    }
}
