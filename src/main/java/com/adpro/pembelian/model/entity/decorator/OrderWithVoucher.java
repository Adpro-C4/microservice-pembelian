package com.adpro.pembelian.model.entity.decorator;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItem;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class OrderWithVoucher extends OrderDecorator {
    @Embedded
    private DTOVoucher voucher;

    public OrderWithVoucher(Order request, DTOVoucher voucher) {
        super(request);
        this.voucher = voucher;
    }

    public OrderWithVoucher() {

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
