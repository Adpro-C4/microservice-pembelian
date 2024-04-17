package com.adpro.pembelian.model;

import java.util.List;

public interface PurchaseRequest {
    String getId();
    String getTimestamp();
    CustomerDetails getCustomerDetails();
    String getAddress();
    List<CartItem> getCartItems();

    double getTotalPrice();
}
