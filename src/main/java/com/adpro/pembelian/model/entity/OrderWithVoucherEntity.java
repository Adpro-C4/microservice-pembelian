package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.dto.DTOVoucher;
import jakarta.persistence.Embedded;

public class OrderWithVoucherEntity extends OrderTemplate{
    @Embedded
    DTOVoucher voucher;
    OrderWithVoucherEntity(){};
    OrderWithVoucherEntity(DTOVoucher voucher){
        this.voucher = voucher;
    }
    @Override
    public double getTotalPrice() {
        return 0;
    }
    public OrderWithVoucherDecorator getDecorator(){
        return  new OrderWithVoucherDecorator(this, voucher);
    }
}
