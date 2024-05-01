package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.dto.DTOVoucher;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderWithVoucherEntity extends OrderTemplate{
    @Embedded
    DTOVoucher voucher;

    OrderWithVoucherEntity(DTOVoucher voucher){
        this.voucher = voucher;
    }

    public OrderWithVoucherEntity() {

    }

    @Override
    public double getTotalPrice() {
        return 0;
    }
    public OrderWithVoucherDecorator getDecorator(){
        return  new OrderWithVoucherDecorator(this, voucher);
    }
}
