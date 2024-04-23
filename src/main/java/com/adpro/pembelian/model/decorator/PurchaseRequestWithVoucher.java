package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.decorator.PurchaseRequestDecorator;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOPurchaseRequest;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PurchaseRequestWithVoucher extends PurchaseRequestDecorator {
    private DTOVoucher voucher;
    public PurchaseRequestWithVoucher(DTOPurchaseRequest request, DTOVoucher voucher) {
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
