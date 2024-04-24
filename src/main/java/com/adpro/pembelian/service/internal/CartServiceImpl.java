package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.model.entity.CartItem;
import com.adpro.pembelian.model.builder.CartItemBuilder;
import com.adpro.pembelian.model.entity.ShoppingCart;
import com.adpro.pembelian.model.builder.ShoppingCartBuilder;
import com.adpro.pembelian.repository.CartItemRepository;
import com.adpro.pembelian.repository.ShoppingCartRepository;
import com.adpro.pembelian.service.internal.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItem createOrUpdateCartItemToShoppingCart(DTOCartItemUpdateInformation cartInformation) {
        ShoppingCart shoppingCart = getShoppingCart(cartInformation.userId());
        if(shoppingCart == null){
            createShoppingCart(cartInformation.userId());
            shoppingCart = getShoppingCart(cartInformation.userId());
        }
        CartItem item = shoppingCart.getCartItemMap().get(cartInformation.productId());
        if(item == null){
            shoppingCart.getCartItemMap().put(cartInformation.productId(),
                    new CartItemBuilder().
                            withName(cartInformation.name()).
                            withPrice(Double.parseDouble(cartInformation.price())).
                            withProductId(cartInformation.productId()).
                            withQuantity(Integer.parseInt(cartInformation.quantity())).
                            build());
        }
        else{
            item.setQuantity(Integer.parseInt(cartInformation.quantity()));
            item.setName(cartInformation.name());
            item.setPrice(Double.parseDouble(cartInformation.price()));
        }
        assert item != null;
        if(item.getQuantity() == 0){
            deleteCartItemFromShoppingCart(
                    new DTOCartItemDeletionInformation
                            (cartInformation.userId(),
                                    cartInformation.productId()));
        }
        shoppingCartRepository.save(shoppingCart);
        return  item;
    }
    @Override
    public void deleteCartItemFromShoppingCart(DTOCartItemDeletionInformation information) {
        ShoppingCart shoppingCart = getShoppingCart(information.userId());
        shoppingCart.getCartItemMap().remove(information.productId());
        shoppingCartRepository.save(shoppingCart);
    }
    @Override
    public List<CartItem> getCartItemsFromShoppingCart(String userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        if(shoppingCart == null){
            throw new RuntimeException("Tidak ada shopping cart yang berasosiasi dengan user dengan id "+ userId);
        }
        return new ArrayList<>(shoppingCart.getCartItemMap().values());
    }

    @Override
    public ShoppingCart getShoppingCart(String userId){
        return  shoppingCartRepository.findById(Long.parseLong(userId)).orElse(null);
    }

    @Override
    public DTOShoppingCartInformation getShoppingCartInformation(String userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        if(shoppingCart == null) {
            throw new RuntimeException("Tidak ada shopping cart yang berasosiasi dengan id" + userId);
        }
        return new DTOShoppingCartInformation(shoppingCart.calculateTotalPrice(),
                shoppingCart.getCartItemMap().values().stream().map(CartItem::toDTO).toList(),
                userId);
    }


    @Override
    public void createShoppingCart(String userId) {
        ShoppingCart cart = getShoppingCart(userId);
        if(cart == null){
            shoppingCartRepository.save(
                    new ShoppingCartBuilder().withCartItems
                            (new HashMap<>()).
                            withUserId(Long.parseLong(userId)).build());
        }
    }
}
