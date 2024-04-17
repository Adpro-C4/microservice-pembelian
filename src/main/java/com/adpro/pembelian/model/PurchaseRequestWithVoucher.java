package com.adpro.pembelian.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PurchaseRequestWithVoucher extends PurchaseRequestDecorator{
    private Voucher voucher;
    public PurchaseRequestWithVoucher(PurchaseRequest request, Voucher voucher) {
        super(request);
        this.voucher = voucher;
    }

    @Override
    public String getId() {
        return this.request.getId();
    }

    @Override
    public String getTimestamp() {
        return this.request.getTimestamp();
    }

    @Override
    public CustomerDetails getCustomerDetails() {
        return this.request.getCustomerDetails();
    }

    @Override
    public String getAddress() {
        return this.request.getAddress();
    }

    @Override
    public List<CartItem> getCartItems() {
        return this.request.getCartItems();
    }
    @Override
    public double getTotalPrice() {
        return this.request.getTotalPrice() - this.request.getTotalPrice() * voucher.getVoucherDiscount();
    }
}
