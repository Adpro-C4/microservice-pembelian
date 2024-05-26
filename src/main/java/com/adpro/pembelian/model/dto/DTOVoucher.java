package com.adpro.pembelian.model.dto;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DTOVoucher implements Serializable {
    private  Long voucherId;
    private String voucherName;
    private String voucherDescription;
    private double voucherDiscount;
    private Integer voucherQuota;
}
