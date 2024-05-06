package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.entity.OrderTemplate;

import java.util.List;

public interface PurchaseService {
    void createPurchaseRequest(DTOPurchaseInformation request);
    void removePurchaseRequest(String orderId);
    OrderTemplate viewOrder(String orderId);
    void removePurchaseRequestByUserId(String userId);
    List<OrderTemplate> viewAllOrderByUserId(String userId);
}
