package com.adpro.pembelian.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.service.internal.PurchaseService;

public class OrderControllerTest {
    final DTOPurchaseInformation purchaseInformationValid = new DTOPurchaseInformation("TEST", 
    new ArrayList<>(Arrays.asList("11,44,22".split(","))), 
    "1", "Depok, Jawa Barat", "JTE");
    final DTOPurchaseInformation purchaseInformationInvalidBcsUserNotFound = new 
    DTOPurchaseInformation("INVALID", 
    new ArrayList<>(Arrays.asList("11,44,22".split(","))), 
    "1", "Depok, Jawa Barat", "JTE");
    final DTOPurchaseInformation purchaseInformationInvalidBcsProductEmpty = new DTOPurchaseInformation("TEST", 
    new ArrayList<>(), "1", "Depok, Jawa Barat", "JTE");
    final DTOPurchaseInformation purchaseInformationInvalidBcsShippingNotValid = new DTOPurchaseInformation("TEST", 
    new ArrayList<>(Arrays.asList("11,44,22".split(","))), 
    "1", "Depok, Jawa Barat", "ABCDE");

    @Mock
    PurchaseService purchaseService;
    @InjectMocks
    OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown(){
        try {
           purchaseService.removePurchaseRequestByUserId("TEST");
        }
        catch (Exception err) {
            System.out.println(err);
        }
    }


    @Test
    void createOrderSuccess(){
       
        ResponseEntity<Object> response =  orderController.createOrder(purchaseInformationValid);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(purchaseService, times(1)).createPurchaseRequest(any(DTOPurchaseInformation.class));
    }

    @Test
    void createOrderFailedBcsProductEmpty(){
        doThrow(new IllegalArgumentException()).when(purchaseService)
        .createPurchaseRequest(purchaseInformationInvalidBcsProductEmpty);
        ResponseEntity<Object> response =  orderController.createOrder(purchaseInformationInvalidBcsProductEmpty);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    void createOrderFailedBcsUserNotFound(){
        doThrow(new NoSuchElementException()).when(purchaseService).
        createPurchaseRequest(purchaseInformationInvalidBcsUserNotFound);
        ResponseEntity<Object> response =  orderController.createOrder(purchaseInformationInvalidBcsUserNotFound);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void createOrderFailedBcsInvalidShipping(){
        doThrow(new IllegalArgumentException()).when(purchaseService)
        .createPurchaseRequest(purchaseInformationInvalidBcsShippingNotValid);
        ResponseEntity<Object> response =  orderController.createOrder(purchaseInformationInvalidBcsShippingNotValid);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
    @Test
    void removeOrderExists(){
        doNothing().when(purchaseService).removePurchaseRequest(anyString());
        ResponseEntity<Object> response =  orderController.removeOrder("123");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(purchaseService, times(1)).removePurchaseRequest(anyString());
    }

    @Test
    void removeOrderNotExist(){
        doThrow(new NoSuchElementException()).when(purchaseService).removePurchaseRequest(anyString());
        ResponseEntity<Object> response =  orderController.removeOrder("123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(purchaseService, times(1)).removePurchaseRequest(anyString());
    }

    @Test
    void viewUserOrdersSuccess(){
        when(purchaseService.viewAllOrderByUserId(anyString())).thenReturn(new ArrayList<>());
        ResponseEntity<Object> response =  orderController.viewUserOrders("123");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(purchaseService, times(1)).viewAllOrderByUserId(anyString());
    }

    @Test
    void viewUserOrdersFailedBcsUserNotFound(){
        doThrow(new NoSuchElementException()).when(purchaseService).viewAllOrderByUserId(anyString());
        ResponseEntity<Object> response =  orderController.viewUserOrders("123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(purchaseService, times(1)).viewAllOrderByUserId(anyString());
    }

    @Test
    void viewOrderSuccess(){
        when(purchaseService.viewOrder(anyString())).thenReturn(new OrdinaryOrderEntity());
        ResponseEntity<Object> response =  orderController.viewOrder("123");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(purchaseService, times(1)).viewOrder(anyString());
    }

    @Test
    void viewOrderFailedBcsOrderNotExist(){
        doThrow(new NoSuchElementException()).when(purchaseService).viewOrder(anyString());
        ResponseEntity<Object> response =  orderController.viewOrder("123");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(purchaseService, times(1)).viewOrder(anyString());
    }
}
