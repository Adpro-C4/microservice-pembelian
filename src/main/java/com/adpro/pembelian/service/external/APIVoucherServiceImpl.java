package com.adpro.pembelian.service.external;

import com.adpro.pembelian.model.builder.VoucherBuilder;
import com.adpro.pembelian.model.dto.DTOVoucher;
import org.springframework.stereotype.Service;

@Service
public class APIVoucherServiceImpl implements APIVoucherService {

    // masih dummy
    // belum sempat konek ke microservice voucher
    @Override
    public DTOVoucher getVoucher(String voucherId) {
        return new VoucherBuilder().
                voucherId(Long.parseLong(voucherId)).
                voucherName("TEST VOUCHER").
                voucherDescription("DESKRIPSI").
                voucherDiscount(0.2).
                voucherQuota(60)
                .build();
    }
}
