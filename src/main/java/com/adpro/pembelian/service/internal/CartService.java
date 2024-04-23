package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.model.entity.ShoppingCart;

import java.util.List;

public interface CartService {
    CartItem createOrUpdateCartItemToShoppingCart(DTOCartItemUpdateInformation cartInformation);
    void deleteCartItemFromShoppingCart(DTOCartItemDeletionInformation information);
    List<CartItem> getCartItemsFromShoppingCart(String userId);
    void createShoppingCart(String userId);
    ShoppingCart getShoppingCart(String userId);

    DTOShoppingCartInformation getShoppingCartInformation(String userId);

}
