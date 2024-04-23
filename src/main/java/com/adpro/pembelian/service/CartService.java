package com.adpro.pembelian.service;

import com.adpro.pembelian.model.*;

import java.util.List;

public interface CartService {
    CartItem createOrUpdateCartItemToShoppingCart(CartItemUpdateInformation cartInformation);
    void deleteCartItemFromShoppingCart(CartItemDeletionInformation information);
    List<CartItem> getCartItemsFromShoppingCart(String userId);
    void createShoppingCart(String userId);
    ShoppingCart getShoppingCart(String userId);

    ShoppingCartInformation getShoppingCartInformation(String userId);

}
