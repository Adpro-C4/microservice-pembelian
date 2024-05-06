package com.adpro.pembelian.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import com.adpro.pembelian.service.internal.CartService;
import com.adpro.pembelian.service.internal.PurchaseServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    
    @Mock
    private APICustomerDetailsService customerDetailsService;
    @Mock
    private APIVoucherService voucherService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartService cartService;
    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private final String TEST_USER_ID = "123";
    private static final String TEST_ORDER_ID = "1234";
    private final String TEST_VOUCHER_ID = "456";
    private final String TEST_ADDRESS = "Test Address";
    private final String TEST_SHIPPING_METHOD = "JTE";
    private final String TEST_SHIPPING_METHOD_INVALID = "ABCDE";
    private final String TEST_PRODUCT_ID_1 = "789";
    private final String TEST_PRODUCT_ID_2 = "101112";

    @Test
    void createOrderSuccess(){
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Arrays.asList(TEST_PRODUCT_ID_1, TEST_PRODUCT_ID_2),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD);
        when(customerDetailsService.getUserDetailsAPI(anyString())).thenReturn(new DTOCustomerDetails());
        purchaseService.createPurchaseRequest(request);
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void createOrderFailedBcsProductEmpty() {
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Collections.emptyList(),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD);
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchaseRequest(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrderFailedBcsUserNotFound() {
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(null);
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, 
        new ArrayList<>(Arrays.asList("ABC", "XYZ")),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD);
        assertThrows(NoSuchElementException.class, () -> purchaseService.createPurchaseRequest(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrderFailedBcsInvalidShipping() {
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(customerDetails);
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Arrays.asList("product1", "product2"),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD_INVALID);
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchaseRequest(request));
        verify(orderRepository, never()).save(any());
    }
    @Test
    void removeOrderExists() {
      
        OrderTemplate order = new OrdinaryOrderEntity();
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.of(order));
      
        purchaseService.removePurchaseRequest(TEST_ORDER_ID);
       
        verify(orderRepository, times(1)).deleteById(Long.parseLong(TEST_ORDER_ID));
    }
    @Test
    void removeOrderNotExist() {
   
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> purchaseService.removePurchaseRequest(TEST_ORDER_ID));

        
        verify(orderRepository, never()).deleteById(anyLong());
    }
    @Test
    void viewUserOrdersSuccess() {
      
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(customerDetails);
        List<OrderTemplate> orders = List.of(new OrdinaryOrderEntity(), new OrdinaryOrderEntity());
        when(orderRepository.findByUserId(TEST_USER_ID)).thenReturn(orders);

      
        List<OrderTemplate> result = purchaseService.viewAllOrderByUserId(TEST_USER_ID);

        assertEquals(orders, result);

     
        verify(customerDetailsService, times(1)).getUserDetailsAPI(TEST_USER_ID);
        verify(orderRepository, times(1)).findByUserId(TEST_USER_ID);
    }

    @Test
    void viewUserOrdersUserNotFound() {
  
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(null);

     
        assertThrows(NoSuchElementException.class, () -> purchaseService.viewAllOrderByUserId(TEST_USER_ID));

        verify(customerDetailsService, times(1)).getUserDetailsAPI(TEST_USER_ID);
        verify(orderRepository, never()).findByUserId(anyString());
    }

    
    @Test
    void viewOrderSuccess() {
  
        OrderTemplate order = new OrdinaryOrderEntity();
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.of(order));

   
        OrderTemplate result = purchaseService.viewOrder(TEST_ORDER_ID);


        assertEquals(order, result);

        verify(orderRepository, times(1)).findById(Long.parseLong(TEST_ORDER_ID));
    }

    @Test
    void viewOrderNotFound() {
   
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.empty());

   
        assertThrows(NoSuchElementException.class, () -> purchaseService.viewOrder(TEST_ORDER_ID));

        verify(orderRepository, times(1)).findById(Long.parseLong(TEST_ORDER_ID));
    }

}
