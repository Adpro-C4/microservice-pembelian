package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.entity.Order;

import java.util.List;

public interface PurchaseService {
    void createPurchaseRequest(DTOPurchaseInformation request);
    void removePurchaseRequest(String orderId);

    Order viewOrder(String orderId);

    List<Order> viewAllOrderByUserId(String userId);
}
