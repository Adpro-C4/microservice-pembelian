package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.entity.OrderTemplate;



public abstract class OrderDecorator extends OrderTemplate {
    protected OrderTemplate request;

    public OrderDecorator(OrderTemplate request){

        this.request = request;
    }

    public OrderDecorator() {

    }

}
