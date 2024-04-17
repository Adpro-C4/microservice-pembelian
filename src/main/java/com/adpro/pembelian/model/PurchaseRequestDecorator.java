package com.adpro.pembelian.model;

public abstract class PurchaseRequestDecorator implements PurchaseRequest {
    protected PurchaseRequest request;
    public PurchaseRequestDecorator(PurchaseRequest request){
        this.request = request;
    }
}
