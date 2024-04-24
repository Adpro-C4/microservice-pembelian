package com.adpro.pembelian.model.dto;

import com.adpro.pembelian.enums.VoucherType;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DTOVoucher {
    private  Long voucherId;
    private String voucherName;
    private String voucherDescription;
    private double voucherDiscount;
    private Integer voucherQuota;
}