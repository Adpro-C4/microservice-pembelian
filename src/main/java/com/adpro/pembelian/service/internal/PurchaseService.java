package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.entity.OrderTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PurchaseService {
    CompletableFuture<Void> createPurchaseRequest(DTOPurchaseInformation request);
    void removePurchaseRequest(String orderId);
    OrderTemplate viewOrder(String orderId);
    void removePurchaseRequestByUserId(String userId);
    List<OrderTemplate> viewAllOrderByUserId(String userId);
}
