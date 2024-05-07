package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CartService {
    CartItemEntity createOrUpdateCartItemToShoppingCart(DTOCartItemUpdateInformation cartInformation);
    void deleteCartItemFromShoppingCart(DTOCartItemDeletionInformation information);
    List<CartItemEntity> getCartItemsFromShoppingCart(String userId);
    CompletableFuture<Void> createShoppingCart(String userId);
    ShoppingCartEntity getShoppingCart(String userId);

    DTOShoppingCartInformation getShoppingCartInformation(String userId);

    void deleteShoppingCart(String userId);

}
