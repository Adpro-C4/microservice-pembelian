package com.adpro.pembelian.model.dto;

import lombok.Getter;

@Getter
public record DTOCartItemUpdateInformation(String userId, String name, String productId, String price , String quantity) {
}
