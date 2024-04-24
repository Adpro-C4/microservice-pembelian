package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOPurchaseInformation;

public interface PurchaseService {
    void createPurchaseRequest(DTOPurchaseInformation request);
}
