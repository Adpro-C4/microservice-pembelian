package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderWithVoucher extends OrderDecorator {
    private DTOVoucher voucher;
    public OrderWithVoucher(Order request, DTOVoucher voucher) {
        super(request);
        this.voucher = voucher;
    }

    @Override
    public String getUserId() {
        return this.request.getUserId();
    }

    @Override
    public String getTimestamp() {
        return this.request.getTimestamp();
    }

    @Override
    public void setPrice(String price) {
        this.request.setPrice(price);
    }

    @Override
    public DTOCustomerDetails getCustomerDetails() {
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
