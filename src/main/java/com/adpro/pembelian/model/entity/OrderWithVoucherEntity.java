package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.service.internal.VoucherCartPricingStrategy;
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

    public OrderWithVoucherEntity(DTOVoucher voucher){
        this.voucher = voucher;
        this.strategy = new VoucherCartPricingStrategy(voucher);
    }

    public OrderWithVoucherEntity() {}

    public OrderWithVoucherEntity(OrderTemplate ordinaryPurchaseRequest, DTOVoucher voucher) {
        this.setData(ordinaryPurchaseRequest);
        setVoucher(voucher);
        this.strategy = new VoucherCartPricingStrategy(voucher);
    }

    @Override
    public double getTotalPrice() {
        this.strategy = new VoucherCartPricingStrategy(voucher);
        return this.strategy.calculateTotalPrice(this.cartItems);
    }
    public OrderWithVoucherDecorator getDecorator(){
        return  new OrderWithVoucherDecorator(this, voucher);
    }
}
