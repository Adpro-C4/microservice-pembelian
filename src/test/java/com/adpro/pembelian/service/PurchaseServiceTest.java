package com.adpro.pembelian.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.adpro.pembelian.common.ShippingUtility;
import com.adpro.pembelian.eventdriven.RabbitMQProducer;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.model.dto.DTOTrackingOrder;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrderWithVoucherEntity;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import com.adpro.pembelian.service.internal.CartService;
import com.adpro.pembelian.service.internal.PurchaseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    
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
    @Mock
    RabbitMQProducer rabbit;

    private final String TEST_USER_ID = "123";
    private static final String TEST_ORDER_ID = "1234";
    private final String TEST_VOUCHER_ID = "456";
    private final String TEST_ADDRESS = "Test Address";
    private final String TEST_SHIPPING_METHOD = "JTE";
    private final String TEST_SHIPPING_METHOD_INVALID = "ABCDE";
    private final String TEST_PRODUCT_ID_1 = "789";
    private final String TEST_PRODUCT_ID_2 = "101112";


    private DTOPurchaseInformation request;

    @BeforeEach
    void setUp() {
        request = new DTOPurchaseInformation("user-1", List.of("item-1", "item-2"),
         "voucher-1", "address-1", "Go-bek");
    }


    @Test
    void testViewAllOrder_NotEmpty() {
        // Setup
        OrdinaryOrderEntity order1 = new OrdinaryOrderEntity();
        order1.setId("1");
        order1.setUserId("user1");
        order1.setShippingMethod("Standard");
        order1.setResi("resi1");
        order1.setAddress("Address 1");
        order1.setPrice("100");

        OrdinaryOrderEntity order2 = new OrdinaryOrderEntity();
        order2.setId("2");
        order2.setUserId("user2");
        order2.setShippingMethod("Express");
        order2.setResi("resi2");
        order2.setAddress("Address 2");
        order2.setPrice("200");

        List<OrderTemplate> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // Exercise
        List<OrderTemplate> result = purchaseService.viewAllOrder();

        // Verify
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("user1", result.get(0).getUserId());
        assertEquals("2", result.get(1).getId());
        assertEquals("user2", result.get(1).getUserId());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testViewAllOrder_Empty() {
        // Setup
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Exercise
        List<OrderTemplate> result = purchaseService.viewAllOrder();

        // Verify
        assertTrue(result.isEmpty());

        verify(orderRepository, times(1)).findAll();
    }
    @Test
    void testCreatePurchaseRequest() throws ExecutionException, InterruptedException {
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        customerDetails.setFullname("User Name");
        customerDetails.setUsername("username");
        customerDetails.setUserId("112");
        customerDetails.setPhoneNumber("123456789");
        customerDetails.setEmail("email@example.com");
        String trackingCode = "TRACK12345";
        when(customerDetailsService.getUserDetailsAPI(anyString())).thenReturn(customerDetails);
        try (var shippingMock = Mockito.mockStatic(ShippingUtility.class)) {
            shippingMock.when(() -> ShippingUtility.generateTrackingCode(anyString())).thenReturn(trackingCode);

          CompletableFuture<Void> future = purchaseService.createPurchaseRequest(request);
          future.join();

        }
        verify(customerDetailsService, times(2)).getUserDetailsAPI(anyString());
        
    }

    @Test
    void testBuildOrderRequest_WithoutVoucherAndMoreThanFiveItems() {
        // Mock data
        List<String> cartItems = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            cartItems.add("item-" + i);
        }
        DTOPurchaseInformation request = new DTOPurchaseInformation("user-1", cartItems, null, "address-1", "Go-bek");
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        String resi = "TRACK12345";

        // Mock behavior
        List<CartItemEntity> mockCartItems = new ArrayList<>();
        for (String productId : cartItems) {
            mockCartItems.add(new CartItemEntity(null, productId, productId, 42, 40));
        }
        when(cartService.getCartItemsFromShoppingCart(anyString())).thenReturn(mockCartItems);

        // Call the method under test
        OrderTemplate orderRequest = purchaseService.buildOrderRequest(request, "2024-05-20T12:00:00Z", customerDetails, resi);

        // Assertion
        assertEquals(OrdinaryOrderEntity.class, orderRequest.getClass());
    }

    @Test
    void testBuildOrderRequest_WithVoucherAndMoreThanFiveItems() {
        // Mock data
        List<String> cartItems = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            cartItems.add("item-" + i);
        }
        DTOPurchaseInformation request = new DTOPurchaseInformation("user-1", cartItems, "voucher-1", "address-1", "Go-bek");
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        String resi = "TRACK12345";

        // Mock behavior
        List<CartItemEntity> mockCartItems = new ArrayList<>();
        for (String productId : cartItems) {
            mockCartItems.add(new CartItemEntity(null, productId, productId, 42, 40));
        }
        when(cartService.getCartItemsFromShoppingCart(anyString())).thenReturn(mockCartItems);
        when(voucherService.getVoucher(anyString())).thenReturn(new DTOVoucher());

        // Call the method under test
        OrderTemplate orderRequest = purchaseService.buildOrderRequest(request, "2024-05-20T12:00:00Z", customerDetails, resi);

        // Assertion
        assertEquals(OrderWithVoucherEntity.class, orderRequest.getClass());
    }

    @Test
    void createOrderSuccess(){
        DTOPurchaseInformation request = new DTOPurchaseInformation(
                TEST_USER_ID,
                Arrays.asList(TEST_PRODUCT_ID_1, TEST_PRODUCT_ID_2),
                TEST_VOUCHER_ID,
                TEST_ADDRESS,
                TEST_SHIPPING_METHOD
        );

        when(customerDetailsService.getUserDetailsAPI(anyString())).thenReturn(new DTOCustomerDetails());

        assertDoesNotThrow(() -> {
            CompletableFuture<Void> future = purchaseService.createPurchaseRequest(request);
            future.join();
        });
    }

    @Test
    void testSendTrackingOrder_Success() throws IOException {
        // Arrange
        OrderTemplate orderRequest = new OrdinaryOrderEntity();
        orderRequest.setId("order-123");
        orderRequest.setShippingMethod("Go-bek");

        DTOTrackingOrder expectedTrackingOrder = DTOTrackingOrder.builder()
                .orderId(orderRequest.getId())
                .methode(orderRequest.getShippingMethod())
                .resiCode("auto")
                .build();

        // Mocking
        doNothing().when(rabbit).sendMessage(anyString(), anyString());

        // Act
        purchaseService.sendTrackingOrder(orderRequest);

        // Assert
        verify(rabbit, times(1)).sendMessage("tracking-order-routing-key", new ObjectMapper().writeValueAsString(expectedTrackingOrder));
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
    void testSendTrackingOrderWithNullOrderRequest() {
        // Arrange
        OrderTemplate orderRequest = null;

        

        // Act & Assert
        assertDoesNotThrow( () -> purchaseService.sendTrackingOrder(orderRequest));
    }

    @Test
    void testCreatePurchaseRequest_WithNullCartItems() {
        // Mock data
        DTOPurchaseInformation request = new DTOPurchaseInformation("user-1", null, "voucher-1", "address-1", "Go-bek");
        
        

        // Assertion
        assertThrows(IllegalArgumentException.class, () -> purchaseService.createPurchaseRequest(request));
    }


    @Test
    void testRemovePurchaseRequestByUserId() {
        // Arrange
        String userId = "user123";

        // Act
        purchaseService.removePurchaseRequestByUserId(userId);

        // Assert
        verify(orderRepository, times(1)).deleteByUserId(userId);
    }
    
    @Test
    void testRemovePurchaseRequestWhenOrderDoesNotExist() {
        // Arrange
        String orderId = "order123";

        // Stubbing repository behavior
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> purchaseService.removePurchaseRequest(orderId));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
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
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
      
        purchaseService.removePurchaseRequest(TEST_ORDER_ID);
       
        verify(orderRepository, times(1)).deleteById(TEST_ORDER_ID);
    }
    @Test
    void removeOrderNotExist() {
   
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> purchaseService.removePurchaseRequest(TEST_ORDER_ID));

        
        verify(orderRepository, never()).deleteById(anyString());
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
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));

   
        OrderTemplate result = purchaseService.viewOrder(TEST_ORDER_ID);


        assertEquals(order, result);

        verify(orderRepository, times(1)).findById(TEST_ORDER_ID);
    }

    @Test
    void viewOrderNotFound() {
   
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

   
        assertThrows(NoSuchElementException.class, () -> purchaseService.viewOrder(TEST_ORDER_ID));

        verify(orderRepository, times(1)).findById(TEST_ORDER_ID);
    }

}
