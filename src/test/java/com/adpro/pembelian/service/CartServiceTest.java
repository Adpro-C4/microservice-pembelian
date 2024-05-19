package com.adpro.pembelian.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adpro.pembelian.model.builder.ShoppingCartBuilder;
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
    private ShoppingCartEntity shoppingCart;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private DTOCartItemUpdateInformation cartInformation;

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
    void testFindOrCreateCartItemDoesNotExist() {
        // Mock data
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity.setCartItemMap(new HashMap<>());
        DTOCartItemUpdateInformation information = new DTOCartItemUpdateInformation
        (TEST_USER_ID, TEST_PRODUCT_NAME, TEST_PRODUCT_ID, TEST_PRICE, TEST_QUANTITY);

        CartItemEntity item = cartService.findOrCreateCartItem(shoppingCartEntity, information);

        assertNotNull(item);
    }
    
    @Test
    void testCreateShoppingCart_CartIsNull() throws InterruptedException, ExecutionException, TimeoutException {
        // Mock data
        String userId = "12";  
        // Mock behavior
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(new DTOCustomerDetails());
        when(shoppingCartRepository.save(any())).thenReturn(null);
        
        // Call the method under test
        CompletableFuture<Void> future = cartService.createShoppingCart(userId);
        
        // Wait for the asynchronous operations to complete
        future.get(5, TimeUnit.SECONDS);

        // Verify behavior
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, times(1)).save(any());

      
       
    }

    @Test
    void testCreateShoppingCart_CartDoesNotExist() {
        // Mock data
        String userId = "122";

        // Mock behavior
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(new DTOCustomerDetails());
        when(shoppingCartRepository.save(any())).thenReturn(null);

        // Call the method under test
        CompletableFuture<Void> future = cartService.createShoppingCart(userId);

        // Verify behavior
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        

        // Ensure the future completes without exceptions
        future.join();
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

   
        assertThrows(java.lang.NullPointerException.class, () -> cartService.createOrUpdateCartItemToShoppingCart(cartInformation));

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
        CompletableFuture<Void> future = cartService.createShoppingCart(userId);
        future.join();
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testCreateShoppingCartUserNotExist() {
        String userId = TEST_USER_ID;
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(null);
        CompletableFuture<Void> future = cartService.createShoppingCart(userId);
        assertThrows(CompletionException.class, () -> future.join());
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); 
    }


    @Test
    void testDeleteShoppingCartItemExists() {

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
    void testDeleteShoppingCartItemNotExist() {
  
        String userId = TEST_USER_ID;
        String productId = TEST_PRODUCT_ID;
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(userId, productId);

        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty()); 
    
        assertThrows(NoSuchElementException.class, () -> cartService.deleteCartItemFromShoppingCart(cartInformation));

        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); 
    }

    @Test
    void testDeleteShoppingCartExists() {
        String userId = TEST_USER_ID;

        // Mock behavior for repository
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.of(shoppingCart));

        // Call the method to test
        cartService.deleteShoppingCart(userId);

        // Verify that deleteById was called with the correct ID
        verify(shoppingCartRepository, times(1)).deleteById(Long.parseLong(userId));
    }

    @Test
    void testDeleteShoppingCartNotExist() {
        String userId = TEST_USER_ID;

        // Mock behavior for repository to return empty
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty());

        // Call the method to test
        assertThrows(NoSuchElementException.class, () -> cartService.deleteShoppingCart(userId));

        // Verify that deleteById was never called
        verify(shoppingCartRepository, never()).deleteById(Long.parseLong(userId));
    }

    @Test
    void testGetCartItemsFromShoppingCart_ShoppingCartExistsWithItems() {
        // Arrange
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        CartItemEntity cartItem = new CartItemEntity();
        cartItemMap.put(TEST_PRODUCT_ID, cartItem);
        shoppingCart.setCartItemMap(cartItemMap);

        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.of(shoppingCart));

        // Act
        List<CartItemEntity> cartItems = cartService.getCartItemsFromShoppingCart(TEST_USER_ID);

        // Assert
        assertEquals(1, cartItems.size());
        assertEquals(cartItem, cartItems.get(0));
    }

    // ShoppingCart does not exist
    @Test
    void testCreateOrUpdateCartItemQuantityZero() {
        // Arrange
        String quantityZero = "0";
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
                TEST_USER_ID, TEST_PRODUCT_NAME, TEST_PRODUCT_ID, TEST_PRICE, quantityZero);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.createOrUpdateCartItemToShoppingCart(cartInformation);
        });

        verify(shoppingCartRepository, never()).findById(any(Long.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testCreateOrUpdateCartItemNegativeQuantity() {
        // Arrange
        String negativeQuantity = "-5";
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
                TEST_USER_ID, TEST_PRODUCT_NAME, TEST_PRODUCT_ID, TEST_PRICE, negativeQuantity);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.createOrUpdateCartItemToShoppingCart(cartInformation);
        });

        verify(shoppingCartRepository, never()).findById(any(Long.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testDeleteCartItemFromShoppingCart_ShoppingCartNotFound() {
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);
        when(shoppingCartRepository.findById(Long.parseLong(TEST_USER_ID))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            cartService.deleteCartItemFromShoppingCart(cartInformation);
        });

        verify(shoppingCartRepository, times(1)).findById(Long.parseLong(TEST_USER_ID));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testFindOrCreateCartItem_ItemDoesNotExist() {
        // Mock data
        String productId = "123";
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation("user-1", "Product", productId, "10.0", "2");

        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(new HashMap<>());
        // Call the method under test
        CartItemEntity result = cartService.findOrCreateCartItem(shoppingCart, cartInformation);

        assertEquals(productId, result.getProductId());
    }

    @Test
    void testDeleteCartItemFromShoppingCart_ProductNotFound() {
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(new HashMap<>());

        when(shoppingCartRepository.findById(Long.parseLong(TEST_USER_ID))).thenReturn(Optional.of(shoppingCart));

        assertThrows(NoSuchElementException.class, () -> {
            cartService.deleteCartItemFromShoppingCart(cartInformation);
        });

        verify(shoppingCartRepository, times(1)).findById(Long.parseLong(TEST_USER_ID));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testDeleteCartItemFromShoppingCart_Success() {
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProductId(TEST_PRODUCT_ID);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, cartItem);
        shoppingCart.setCartItemMap(cartItemMap);

        when(shoppingCartRepository.findById(Long.parseLong(TEST_USER_ID))).thenReturn(Optional.of(shoppingCart));

        cartService.deleteCartItemFromShoppingCart(cartInformation);

        assertFalse(shoppingCart.getCartItemMap().containsKey(TEST_PRODUCT_ID));
        verify(shoppingCartRepository, times(1)).findById(Long.parseLong(TEST_USER_ID));
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    // Tests for getCartItemsFromShoppingCart
    @Test
    void testGetCartItemsFromShoppingCart_ShoppingCartNotFound() {
        when(shoppingCartRepository.findById(Long.parseLong(TEST_USER_ID))).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cartService.getCartItemsFromShoppingCart(TEST_USER_ID);
        });

        assertEquals("Tidak ada shopping cart yang berasosiasi dengan user dengan id " + TEST_USER_ID, thrown.getMessage());
        verify(shoppingCartRepository, times(1)).findById(Long.parseLong(TEST_USER_ID));
    }

    @Test
    void testGetCartItemsFromShoppingCart_Success() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProductId(TEST_PRODUCT_ID);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, cartItem);
        shoppingCart.setCartItemMap(cartItemMap);

        when(shoppingCartRepository.findById(Long.parseLong(TEST_USER_ID))).thenReturn(Optional.of(shoppingCart));

        List<CartItemEntity> result = cartService.getCartItemsFromShoppingCart(TEST_USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_PRODUCT_ID, result.get(0).getProductId());
        verify(shoppingCartRepository, times(1)).findById(Long.parseLong(TEST_USER_ID));
    }



}
