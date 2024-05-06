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
import com.adpro.pembelian.enums.ShippingMethod;
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
        purchaseService.createPurchaseRequest(request);
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void createOrderFailedBcsProductEmpty() {
        when(cartService.getCartItemsFromShoppingCart(TEST_USER_ID)).thenReturn(Collections.emptyList());
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Collections.emptyList(),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD);
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchaseRequest(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrderFailedBcsUserNotFound() {
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(null);
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Collections.emptyList(),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD);
        assertThrows(NoSuchElementException.class, () -> purchaseService.createPurchaseRequest(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrderFailedBcsInvalidShipping() {
        // Mock behavior for customerDetailsService to return a valid DTOCustomerDetails
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(customerDetails);

        // Mock data
        DTOPurchaseInformation request = new DTOPurchaseInformation(TEST_USER_ID, Arrays.asList("product1", "product2"),
                TEST_VOUCHER_ID, TEST_ADDRESS, TEST_SHIPPING_METHOD_INVALID);

        // Test method and assert that it throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchaseRequest(request));

        // Verify that no order is saved
        verify(orderRepository, never()).save(any());
    }
    @Test
    void removeOrderExists() {
        // Mock data
        OrderTemplate order = new OrdinaryOrderEntity();
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.of(order));
        // Test method
        purchaseService.removePurchaseRequest(TEST_ORDER_ID);
        // Verify that deleteById is called once with the correct argument
        verify(orderRepository, times(1)).deleteById(Long.parseLong(TEST_ORDER_ID));
    }
    @Test
    void removeOrderNotExist() {
        // Mock behavior for orderRepository to return empty Optional, indicating that order does not exist
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.empty());

        // Test method and assert that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> purchaseService.removePurchaseRequest(TEST_ORDER_ID));

        // Verify that deleteById is never called
        verify(orderRepository, never()).deleteById(anyLong());
    }
    @Test
    void viewUserOrdersSuccess() {
        // Mock data
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(customerDetails);
        List<OrderTemplate> orders = List.of(new OrdinaryOrderEntity(), new OrdinaryOrderEntity());
        when(orderRepository.findByUserId(TEST_USER_ID)).thenReturn(orders);

        // Test method
        List<OrderTemplate> result = purchaseService.viewAllOrderByUserId(TEST_USER_ID);

        // Verify that the method returns the expected list of orders
        assertEquals(orders, result);

        // Verify interactions with mocked objects
        verify(customerDetailsService, times(1)).getUserDetailsAPI(TEST_USER_ID);
        verify(orderRepository, times(1)).findByUserId(TEST_USER_ID);
    }

    @Test
    void viewUserOrdersUserNotFound() {
        // Mock behavior for customerDetailsService to return null, indicating that user is not found
        when(customerDetailsService.getUserDetailsAPI(TEST_USER_ID)).thenReturn(null);

        // Test method and assert that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> purchaseService.viewAllOrderByUserId(TEST_USER_ID));

        // Verify interactions with mocked objects
        verify(customerDetailsService, times(1)).getUserDetailsAPI(TEST_USER_ID);
        verify(orderRepository, never()).findByUserId(anyString());
    }

    
    @Test
    void viewOrderSuccess() {
        // Mock data
        OrderTemplate order = new OrdinaryOrderEntity();
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.of(order));

        // Test method
        OrderTemplate result = purchaseService.viewOrder(TEST_ORDER_ID);

        // Verify that the method returns the expected order
        assertEquals(order, result);

        // Verify interactions with mocked objects
        verify(orderRepository, times(1)).findById(Long.parseLong(TEST_ORDER_ID));
    }

    @Test
    void viewOrderNotFound() {
        // Mock behavior for orderRepository to return empty optional, indicating that order is not found
        when(orderRepository.findById(Long.parseLong(TEST_ORDER_ID))).thenReturn(Optional.empty());

        // Test method and assert that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> purchaseService.viewOrder(TEST_ORDER_ID));

        // Verify interactions with mocked objects
        verify(orderRepository, times(1)).findById(Long.parseLong(TEST_ORDER_ID));
    }

}
