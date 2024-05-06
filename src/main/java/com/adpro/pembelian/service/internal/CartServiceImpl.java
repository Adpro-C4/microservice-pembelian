package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.builder.CartItemBuilder;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;
import com.adpro.pembelian.model.builder.ShoppingCartBuilder;
import com.adpro.pembelian.repository.CartItemRepository;
import com.adpro.pembelian.repository.ShoppingCartRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    APICustomerDetailsService customerDetailsService;

    @Override
    public CartItemEntity createOrUpdateCartItemToShoppingCart(DTOCartItemUpdateInformation cartInformation) {
        validateQuantity(cartInformation.quantity());

        ShoppingCartEntity shoppingCart = findOrCreateShoppingCart(cartInformation.userId());
        CartItemEntity item = findOrCreateCartItem(shoppingCart, cartInformation);
        updateCartItem(item, cartInformation);

        if (item.getQuantity() == 0) {
            deleteCartItemFromShoppingCart(new DTOCartItemDeletionInformation(cartInformation.userId(), 
            cartInformation.productId()));
        }

        shoppingCartRepository.save(shoppingCart);
        return item;
    }

    private void validateQuantity(String quantity) {
        if (Long.parseLong(quantity) <= 0) {
            throw new IllegalArgumentException("Quantity harus bilangan positif");
        }
    }

    private ShoppingCartEntity findOrCreateShoppingCart(String userId) {
        ShoppingCartEntity shoppingCart = getShoppingCart(userId);
        if (shoppingCart == null) {
            createShoppingCart(userId);
            shoppingCart = getShoppingCart(userId);
        }
        return shoppingCart;
    }

    private CartItemEntity findOrCreateCartItem(ShoppingCartEntity shoppingCart, DTOCartItemUpdateInformation cartInformation) {
        CartItemEntity item = shoppingCart.getCartItemMap().get(cartInformation.productId());
        if (item == null) {
            item = createCartItem(cartInformation);
            shoppingCart.getCartItemMap().put(cartInformation.productId(), item);
        }
        return item;
    }

    private CartItemEntity createCartItem(DTOCartItemUpdateInformation cartInformation) {
        return new CartItemBuilder()
                .withName(cartInformation.name())
                .withPrice(Double.parseDouble(cartInformation.price()))
                .withProductId(cartInformation.productId())
                .withQuantity(Integer.parseInt(cartInformation.quantity()))
                .build();
    }

    private void updateCartItem(CartItemEntity item, DTOCartItemUpdateInformation cartInformation) {
        item.setQuantity(Integer.parseInt(cartInformation.quantity()));
        item.setName(cartInformation.name());
        item.setPrice(Double.parseDouble(cartInformation.price()));
    }

    @Override
    public void deleteCartItemFromShoppingCart(DTOCartItemDeletionInformation information) {
        ShoppingCartEntity shoppingCart = getShoppingCart(information.userId());
        if(shoppingCart == null){
            throw new NoSuchElementException();
        }
        if(! shoppingCart.getCartItemMap().containsKey(information.productId())){
            throw new NoSuchElementException();
        }
        shoppingCart.getCartItemMap().remove(information.productId());
        shoppingCartRepository.save(shoppingCart);
    }
    @Override
    public List<CartItemEntity> getCartItemsFromShoppingCart(String userId) {
        ShoppingCartEntity shoppingCart = getShoppingCart(userId);
        if(shoppingCart == null){
            throw new RuntimeException("Tidak ada shopping cart yang berasosiasi dengan user dengan id "+ userId);
        }
        return new ArrayList<>(shoppingCart.getCartItemMap().values());
    }

    @Override
    public ShoppingCartEntity getShoppingCart(String userId){
        return  shoppingCartRepository.findById(Long.parseLong(userId)).orElse(null);
    }

    @Override
    public DTOShoppingCartInformation getShoppingCartInformation(String userId) {
        ShoppingCartEntity shoppingCart = getShoppingCart(userId);
        if(shoppingCart == null) {
            throw new NoSuchElementException("Tidak ada shopping cart yang berasosiasi dengan id" + userId);
        }
        return new DTOShoppingCartInformation(shoppingCart.calculateTotalPrice(),
                shoppingCart.getCartItemMap().values().stream().map(CartItemEntity::toDTO).toList(),
                userId);
    }

    @Override
    public void deleteShoppingCart(String userId) {
        shoppingCartRepository.deleteById(Long.valueOf(userId));
    }


    @Override
    public void createShoppingCart(String userId) {
        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(userId);
        System.out.println(customerDetails);
        if(customerDetails == null){
            throw  new NoSuchElementException();
        }
        ShoppingCartEntity cart = getShoppingCart(userId);
        if(cart == null){
            shoppingCartRepository.save(
                    new ShoppingCartBuilder().withCartItems
                            (new HashMap<>()).
                            withUserId(Long.parseLong(userId)).build());
        }
    }
}
