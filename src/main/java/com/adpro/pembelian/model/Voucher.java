package com.adpro.pembelian.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Voucher {
    private  Long voucherId;
    private String voucherName;
    private String voucherDescription;
    private double voucherDiscount;
    private  String voucherType;
    private Integer voucherQuota;

    public void setVoucherType(String voucherType){
        if(voucherType.equals("Limited") || voucherType.equals("Permanent")){
            this.voucherType = voucherType;
        }else{
            throw new IllegalArgumentException("Tipe voucher tidak sesuai");
        }
    }
}
