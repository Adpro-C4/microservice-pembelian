package com.adpro.pembelian.model.dto;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.entity.CartItem;

import java.util.List;

public interface DTOPurchaseRequest {
    String getId();
    String getTimestamp();
    DTOCustomerDetails getCustomerDetails();
    String getAddress();
    List<CartItem> getCartItems();

    double getTotalPrice();
}
