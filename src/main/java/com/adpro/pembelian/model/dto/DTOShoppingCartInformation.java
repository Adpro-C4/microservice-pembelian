package com.adpro.pembelian.model.dto;
import java.util.List;


public record DTOShoppingCartInformation(double totalPrice,
                                         List<DTOCartItem> dtoCartItemList,
                                         String userId
                                         ) {
}
