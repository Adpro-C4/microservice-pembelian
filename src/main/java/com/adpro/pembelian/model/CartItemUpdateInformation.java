package com.adpro.pembelian.model;

import lombok.Getter;

@Getter
public record CartItemUpdateInformation(String userId, String name, String productId, String price , String quantity) {
}
