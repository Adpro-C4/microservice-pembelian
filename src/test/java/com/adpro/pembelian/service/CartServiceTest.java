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
        // Mock data
        DTOCartItemUpdateInformation cartInformation = new 
        DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            TEST_QUANTITY);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(new HashMap<>());

        // Mock repository behavior
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCart);

        // Test method
        CartItemEntity result = cartService.createOrUpdateCartItemToShoppingCart(cartInformation);

        // Verify repository interactions
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCartEntity.class));

        // Assert result
        assertEquals(TEST_PRODUCT_ID, result.getProductId());
        assertEquals(TEST_PRODUCT_NAME, result.getName());
        assertEquals(Integer.parseInt(TEST_QUANTITY), result.getQuantity());
    }
    @Test
    void testCreateOrUpdateCartItemNull() {
        // Mock data
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            TEST_QUANTITY);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartItemMap(null);

        // Mock repository behavior
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.empty()); // Return Optional.empty() to simulate cart not found
        when(shoppingCartRepository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCart);

        // Test method
        assertThrows(NoSuchElementException.class, () -> cartService.createOrUpdateCartItemToShoppingCart(cartInformation));

        // Verify repository interactions
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }


    @Test
    void testCreateOrUpdateCartItemZeroOrNegative() {
        // Mock data
        DTOCartItemUpdateInformation cartInformation = new DTOCartItemUpdateInformation(
            TEST_USER_ID, 
            TEST_PRODUCT_NAME, 
            TEST_PRODUCT_ID, 
            TEST_PRICE, 
            "0"); // Set quantity to zero

        // Assert that IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> cartService.createOrUpdateCartItemToShoppingCart(cartInformation));

        // Verify that repository interactions do not occur
        verify(shoppingCartRepository, never()).findById(any(Long.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class));
    }


    @Test
    void testDeleteCartItemExists() {
        // Mock data
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);

        // Mock shopping cart with the item to be deleted
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, new CartItemEntity());
        shoppingCart.setCartItemMap(cartItemMap);

        // Mock repository behavior
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.of(shoppingCart));

        // Test method
        cartService.deleteCartItemFromShoppingCart(cartInformation);

        // Verify repository interactions
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testDeleteCartItemNotExist() {
        // Mock data
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);

        // Mock repository behavior
        when(shoppingCartRepository.findById(any(Long.class))).thenReturn(Optional.empty()); // Return Optional.empty() to simulate cart not found

        // Assert that NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> cartService.deleteCartItemFromShoppingCart(cartInformation));

        // Verify repository interactions
        verify(shoppingCartRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetShoppingCartInformationExists() {
        // Mock data
        String userId = TEST_USER_ID;
        Optional<ShoppingCartEntity> shoppingCart = Optional.ofNullable(new ShoppingCartEntity());
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(TEST_PRODUCT_ID, new CartItemEntity());
        shoppingCart.get().setCartItemMap(cartItemMap);

        // Mock repository behavior
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(shoppingCart);

        // Test method
        DTOShoppingCartInformation cartInformation = cartService.getShoppingCartInformation(userId);

        // Verify that the DTOShoppingCartInformation is created with correct data
        assertEquals(0.0, cartInformation.totalPrice()); // Adjust this value according to your test case
        assertEquals(1, cartInformation.dtoCartItemList().size()); // Adjust this value according to your test case
        assertEquals(userId, cartInformation.userId());
    }

    @Test
    void testGetShoppingCartInformationNotExist() {
        // Mock data
        String userId = TEST_USER_ID;

        // Mock repository behavior
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty()); // Return Optional.empty() to simulate cart not found

        // Assert that NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> cartService.getShoppingCartInformation(userId));
    }

    @Test
    void testCreateShoppingCartUserExists() {
        // Mock data
        String userId = TEST_USER_ID;
        DTOCustomerDetails customerDetails = new DTOCustomerDetails(); // Mock customer details

        // Mock repository behavior
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(customerDetails); // Return customer details to simulate user exists

        // Test method
        cartService.createShoppingCart(userId);

        // Verify repository interactions
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCartEntity.class));
    }

    @Test
    void testCreateShoppingCartUserNotExist() {
        // Mock data
        String userId = TEST_USER_ID;

        // Mock repository behavior
        when(customerDetailsService.getUserDetailsAPI(userId)).thenReturn(null); // Return null to simulate user not found

        // Assert that NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> cartService.createShoppingCart(userId));

        // Verify repository interactions
        verify(customerDetailsService, times(1)).getUserDetailsAPI(userId);
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); // Ensure that save method is never called
    }

    @Test
    void testDeleteShoppingCartExists() {
        // Mock data
        String userId = TEST_USER_ID;
        String productId = TEST_PRODUCT_ID;
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(userId, productId);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        Map<String, CartItemEntity> cartItemMap = new HashMap<>();
        cartItemMap.put(productId, new CartItemEntity());
        shoppingCart.setCartItemMap(cartItemMap);

        // Mock repository behavior
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.of(shoppingCart));

        // Test method
        cartService.deleteCartItemFromShoppingCart(cartInformation);

        // Verify repository interactions
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    void testDeleteShoppingCartNotExist() {
        // Mock data
        String userId = TEST_USER_ID;
        String productId = TEST_PRODUCT_ID;
        DTOCartItemDeletionInformation cartInformation = new DTOCartItemDeletionInformation(userId, productId);

        // Mock repository behavior
        when(shoppingCartRepository.findById(Long.parseLong(userId))).thenReturn(Optional.empty()); // Return null to simulate cart not found

        // Assert that NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, () -> cartService.deleteCartItemFromShoppingCart(cartInformation));

        // Verify repository interactions
        verify(shoppingCartRepository, never()).save(any(ShoppingCartEntity.class)); // Ensure that save method is never called
    }


}
