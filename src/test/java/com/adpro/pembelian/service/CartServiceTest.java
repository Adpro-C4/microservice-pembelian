package com.adpro.pembelian.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;
import com.adpro.pembelian.repository.CartItemRepository;
import com.adpro.pembelian.repository.ShoppingCartRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.internal.CartServiceImpl;

public class CartServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private APICustomerDetailsService customerDetailsService;

    @InjectMocks
    private CartServiceImpl cartService;

    private final String TEST_USER_ID = "-8";
    private final String TEST_PRODUCT_ID = "-2";
    private final String TEST_PRODUCT_NAME = "ProductName";
    private final String TEST_PRICE = "20000";
    private final String TEST_QUANTITY = "10";


    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testCreateOrUpdateCartItem() {
   
        DTOCartItemUpdateInformation cartInformation = new 
        DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            TEST_QUANTITY);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(new HashMap<>());


        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCart);

      
        CartItemEntity result = cartService.createOrUpdateCartItemToShoppingCart(cartInformation);

        
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCartEntity.class));

     
        assertEquals(TEST_PRODUCT_ID, result.getProductId());
        assertEquals(TEST_PRODUCT_NAME, result.getName());
        assertEquals(Integer.parseInt(TEST_QUANTITY), result.getQuantity());
    }
    @Test
    void testCreateOrUpdateCartItemNull() {
     
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            TEST_QUANTITY);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(null);

      
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.empty()); 
        when(shoppingCartRepository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCart);

   
        assertThrows(NoSuchElementException.class, () -> cartService.createOrUpdateCartItemToShoppingCart(cartInformation));

     
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }


    @Test
    void testCreateOrUpdateCartItemZeroOrNegative() {
   
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            "0"); 

    
        assertThrows(IllegalArgumentException.class, () -> cartService.createOrUpdateCartItemToShoppingCart(cartInformation));

        verify(shoppingCartRepository, never()).findById(any(Long.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }


    @Test
    void testDeleteCartItemExists() {
   
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);


        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, new CartItemEntity());
        shoppingCart.setCartItemMap(cartItemMap);

    
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.of(shoppingCart));

    
        cartService.deleteCartItemFromShoppingCart(cartInformation);

        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testDeleteCartItemNotExist() {
     
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);

 
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.empty()); 

   
        assertThrows(NoSuchElementException.class, () -> cartService.deleteCartItemFromShoppingCart(cartInformation));

  
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetShoppingCartInformationExists() {
      
        String userId = TEST_USER_ID;
        Optional<ShoppingCartEntity> shoppingCart = Optional.ofNullable(new ShoppingCartEntity());
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, new CartItemEntity());
        shoppingCart.get().setCartItemMap(cartItemMap);

 
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(shoppingCart);


        DTOShoppingCartInformation cartInformation = cartService.getShoppingCartInformation(userId);

       
        assertEquals(0.0, cartInformation.totalPrice()); 
        assertEquals(1, cartInformation.dtoCartItemList().size()); 
        assertEquals(userId, cartInformation.userId());
    }

    @Test
    void testGetShoppingCartInformationNotExist() {

        String userId = TEST_USER_ID;

        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty()); 


        assertThrows(NoSuchElementException.class, () -> cartService.getShoppingCartInformation(userId));
    }

    @Test
    void testCreateShoppingCartUserExists() {
 
        String userId = TEST_USER_ID;
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();


        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(customerDetails); 

        cartService.createShoppingCart(userId);

   
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testCreateShoppingCartUserNotExist() {
        
        String userId = TEST_USER_ID;

       
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(null); 

        assertThrows(NoSuchElementException.class, () -> cartService.createShoppingCart(userId));

        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); 
    }

    @Test
    void testDeleteShoppingCartExists() {

        String userId = TEST_USER_ID;
        String productId = TEST_PRODUCT_ID;
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(userId, productId);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(productId, new CartItemEntity());
        shoppingCart.setCartItemMap(cartItemMap);

        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.of(shoppingCart));

   
        cartService.deleteCartItemFromShoppingCart(cartInformation);

        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    void testDeleteShoppingCartNotExist() {
  
        String userId = TEST_USER_ID;
        String productId = TEST_PRODUCT_ID;
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(userId, productId);

        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty()); 
    
        assertThrows(NoSuchElementException.class, () -> cartService.deleteCartItemFromShoppingCart(cartInformation));

        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); 
    }


}
