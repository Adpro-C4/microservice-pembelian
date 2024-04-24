package com.adpro.pembelian.service.external;

import com.adpro.pembelian.model.dto.DTOVoucher;

public interface APIVoucherService {
    DTOVoucher getVoucher(String voucherId);

}
