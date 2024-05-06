package com.adpro.pembelian.controller;
import com.adpro.pembelian.model.dto.DTOCartItem;
import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.service.internal.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartControllerTest {

    private final String TEST_USER_ID = "-9999999";
    private  final String TEST_PRODUCT_ID = "-99";

    @Mock
    CartService cartService;

    @InjectMocks
    CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getShoppingCartInformationExist() {
        // Mocking
        List<DTOCartItem> dtoCartItemList = new ArrayList<>();
        double totalPrice = 100.0;
        DTOShoppingCartInformation cartInformation =
                new DTOShoppingCartInformation(totalPrice, dtoCartItemList, TEST_USER_ID);
        when(cartService.getShoppingCartInformation(anyString())).thenReturn(cartInformation);
        ResponseEntity<Object> responseEntity = cartController.getShoppingCartInformation(TEST_USER_ID);
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("message");
        assert responseBody.containsKey("data");
        assert responseBody.get("status").equals(HttpStatus.ACCEPTED.value());
        verify(cartService, times(1)).getShoppingCartInformation(anyString());
    }

    @Test
    void getShoppingCartInformationNotExist() {
        // Mocking
        when(cartService.getShoppingCartInformation(anyString())).thenReturn(null);

        // Test
        ResponseEntity<Object> responseEntity = cartController.getShoppingCartInformation(TEST_USER_ID);

        // Verify
        assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("status");
        assert responseBody.get("status").equals(HttpStatus.NOT_FOUND.value());
        verify(cartService, times(1)).getShoppingCartInformation(anyString());
    }


    @Test
    void updateCartItemOnShoppingCart() {
        DTOCartItemUpdateInformation cartItemUpdateInformation = new
                DTOCartItemUpdateInformation(TEST_USER_ID, "ItemName", TEST_PRODUCT_ID,
                "10.0", "2");
        ResponseEntity<Object> responseEntity = cartController.updateCartItemOnShoppingCart(cartItemUpdateInformation);
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("message");
        assert responseBody.get("status").equals(HttpStatus.ACCEPTED.value());
        verify(cartService, times(1)).createOrUpdateCartItemToShoppingCart(any(DTOCartItemUpdateInformation.class));
    }

    @Test
    void updateCartItemOnShoppingCartNotExist() {
        DTOCartItemUpdateInformation cartItemUpdateInformation = new
                DTOCartItemUpdateInformation(TEST_USER_ID, "ItemName",
                TEST_PRODUCT_ID, "10.0", "2");
        ResponseEntity<Object> responseEntity = cartController.updateCartItemOnShoppingCart(cartItemUpdateInformation);
        Assertions.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("status");
        assert responseBody.get("status").equals(HttpStatus.ACCEPTED.value());
        verify(cartService, times(1)).createOrUpdateCartItemToShoppingCart(any(DTOCartItemUpdateInformation.class));
    }

    @Test
    void deleteCartItemFromShoppingCart() {

        DTOCartItemDeletionInformation deletionInformation = new
                DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);
        doNothing().when(cartService).deleteCartItemFromShoppingCart(any(DTOCartItemDeletionInformation.class));
        ResponseEntity<Object> responseEntity = cartController.deleteCartItemFromShoppingCart(deletionInformation);
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("message");
        assert responseBody.get("status").equals(HttpStatus.ACCEPTED.value());
        verify(cartService, times(1)).deleteCartItemFromShoppingCart(any(DTOCartItemDeletionInformation.class));
    }

    @Test
    void deleteCartItemFromShoppingCart_CartItemNotFound() {
        DTOCartItemDeletionInformation deletionInformation =
                new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID);
        ResponseEntity<Object> responseEntity = cartController.deleteCartItemFromShoppingCart(deletionInformation);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("status"));
        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), responseBody.get("status"));
        verify(cartService, times(1)).deleteCartItemFromShoppingCart(any(DTOCartItemDeletionInformation.class));
    }

    @Test
    void createShoppingCart() {
        // Mocking
        JsonNode node = mock(JsonNode.class);
        when(node.get("userId")).thenReturn(mock(JsonNode.class));
        when(node.get("userId").asText()).thenReturn(TEST_USER_ID);

        // Test
        ResponseEntity<Object> responseEntity = cartController.createShoppingCart(node);

        // Verify
        assert responseEntity.getStatusCode() == HttpStatus.ACCEPTED;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("message");
        verify(cartService, times(1)).createShoppingCart(anyString());
    }

    @Test
    void createShoppingCart_CreationUserNotExist() {
        // Mocking
        JsonNode node = mock(JsonNode.class);
        when(node.get("userId")).thenReturn(mock(JsonNode.class));
        when(node.get("userId").asText()).thenReturn(TEST_USER_ID);
        // Test
        ResponseEntity<Object> responseEntity = cartController.createShoppingCart(node);
        // Verify
        assert responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assert responseBody != null;
        assert responseBody.containsKey("status");
        assert responseBody.get("status").equals(HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(cartService, times(1)).createShoppingCart(anyString());
    }

    @AfterEach
    void tearDown() {
        cartService.deleteCartItemFromShoppingCart
                (new DTOCartItemDeletionInformation(TEST_USER_ID, TEST_PRODUCT_ID));
        cartService.deleteShoppingCart(TEST_USER_ID);
    }

}
