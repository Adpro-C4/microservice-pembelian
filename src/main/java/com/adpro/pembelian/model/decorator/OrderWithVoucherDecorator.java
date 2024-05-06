package com.adpro.pembelian.model.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.dto.DTOVoucher;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderWithVoucherDecorator extends OrderDecorator {
    @Embedded
    private DTOVoucher voucher;

    public OrderWithVoucherDecorator(OrderTemplate request, DTOVoucher voucher) {
        super(request);
        this.voucher = voucher;
    }

    public OrderWithVoucherDecorator() {

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
    public double getTotalPrice() {
        return this.request.getTotalPrice() - this.request.getTotalPrice() * voucher.getVoucherDiscount();
    }
}
