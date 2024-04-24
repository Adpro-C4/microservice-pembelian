package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.entity.Order;

public abstract class OrderDecorator extends Order {
    protected Order request;
    public OrderDecorator(Order request){
        this.request = request;
    }
}
