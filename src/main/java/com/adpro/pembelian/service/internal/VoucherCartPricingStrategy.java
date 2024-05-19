package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItemEntity;

import java.util.List;

public class VoucherCartPricingStrategy implements PricingStrategy<CartItemEntity>{
    private DTOVoucher voucher;
    public VoucherCartPricingStrategy(DTOVoucher voucher){
        setVoucher(voucher);
    }
    public VoucherCartPricingStrategy(){}

    public void setVoucher(DTOVoucher voucher) {
        this.voucher = voucher;
    }

    @Override
    public double calculateTotalPrice(List<CartItemEntity> items) {
        double totalPrice = 0;
        for(CartItemEntity item : items ){
            totalPrice+= item.calculateTotalPrice();
        }
        return totalPrice - totalPrice * voucher.getVoucherDiscount();
    }
}
