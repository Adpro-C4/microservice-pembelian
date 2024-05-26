package com.adpro.pembelian.service.external;

import com.adpro.pembelian.enums.ProductAPI;
import com.adpro.pembelian.model.dto.DTOVoucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIVoucherServiceImpl implements APIVoucherService {

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public DTOVoucher getVoucher(String voucherId) {
        return restTemplate.getForObject(ProductAPI.VOUCHER_BY_ID.getUrl()+"?id="+voucherId,
         DTOVoucher.class);
    }
}
