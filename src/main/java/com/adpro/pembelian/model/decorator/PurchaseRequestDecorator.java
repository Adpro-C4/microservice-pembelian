package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.dto.DTOPurchaseRequest;

public abstract class PurchaseRequestDecorator implements DTOPurchaseRequest {
    protected DTOPurchaseRequest request;
    public PurchaseRequestDecorator(DTOPurchaseRequest request){
        this.request = request;
    }
}
