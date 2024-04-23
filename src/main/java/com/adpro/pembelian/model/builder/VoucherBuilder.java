package com.adpro.pembelian.model.builder;

import com.adpro.pembelian.model.dto.DTOVoucher;

public class VoucherBuilder {
    private Long voucherId;
    private String voucherName;
    private String voucherDescription;
    private double voucherDiscount;
    private String voucherType;
    private Integer voucherQuota;

    public VoucherBuilder voucherId(Long voucherId) {
        this.voucherId = voucherId;
        return this;
    }

    public VoucherBuilder voucherName(String voucherName) {
        this.voucherName = voucherName;
        return this;
    }

    public VoucherBuilder voucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
        return this;
    }

    public VoucherBuilder voucherDiscount(double voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
        return this;
    }

    public VoucherBuilder voucherType(String voucherType) {
        this.voucherType = voucherType;
        return this;
    }

    public VoucherBuilder voucherQuota(Integer voucherQuota) {
        this.voucherQuota = voucherQuota;
        return this;
    }

    public DTOVoucher build() {
        DTOVoucher voucher = new DTOVoucher();
        voucher.setVoucherId(this.voucherId);
        voucher.setVoucherName(this.voucherName);
        voucher.setVoucherDescription(this.voucherDescription);
        voucher.setVoucherDiscount(this.voucherDiscount);
        voucher.setVoucherType(this.voucherType);
        voucher.setVoucherQuota(this.voucherQuota);
        return voucher;
    }
}
