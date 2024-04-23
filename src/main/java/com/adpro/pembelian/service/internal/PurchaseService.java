package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOCheckoutRequest;

public interface PurchaseService {
    DTOCheckoutRequest createPurchaseRequest(String userId, String voucherId, String)
}
