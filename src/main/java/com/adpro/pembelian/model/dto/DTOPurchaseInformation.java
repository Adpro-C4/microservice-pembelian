package com.adpro.pembelian.model.dto;

import java.util.List;

public record DTOPurchaseInformation(String userId, List<String> cartItems, String voucherId, String address, String shippingMethod) {
}
