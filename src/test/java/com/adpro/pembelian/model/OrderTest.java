package com.adpro.pembelian.model;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.service.internal.CartPricingStrategy;
import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrderWithVoucherEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OrderTest {
    private OrdinaryOrderEntity order1;
    private OrdinaryOrderEntity order2;
    private OrderWithVoucherEntity orderWithVoucher;
    private DTOVoucher voucher;

    @BeforeEach
    public void setUp() {

        voucher = new DTOVoucher();
        voucher.setVoucherId(1L);
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherDescription("This is a test voucher");
        voucher.setVoucherDiscount(0.10); // 10% discount
        voucher.setVoucherQuota(100);

        orderWithVoucher = new OrderWithVoucherEntity(voucher);

        order1 = new OrdinaryOrderEntity();
        order1.setId("1");
        order1.setUserId("user123");
        order1.setTimestamp("2023-05-18T12:00:00");
        order1.setShippingMethod("Express");
        order1.setResi("RESI12345");

        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        customerDetails.setFullname("John Doe");
        customerDetails.setUsername("johndoe");
        customerDetails.setUserId("user123");
        customerDetails.setPhoneNumber("1234567890");
        customerDetails.setEmail("johndoe@example.com");
        order1.setCustomerDetails(customerDetails);

        order1.setAddress("123 Main St");
        order1.setPrice("100.00");

        CartItemEntity cartItem1 = new CartItemEntity(1L, "P1", "Product 1", 2, 50.0);
        cartItem1.setOrder(order1);
        order1.setCartItems(Arrays.asList(cartItem1));

        order2 = new OrdinaryOrderEntity();
    }

    @Test
    public void testGetTotalPrice() {
        CartItemEntity item1 = new CartItemEntity(1L, "P1", "Product 1", 2, 50.0);
        CartItemEntity item2 = new CartItemEntity(2L, "P2", "Product 2", 3, 30.0);
        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(item1);
        cartItems.add(item2);
        orderWithVoucher.setCartItems(cartItems);

        double totalPrice = orderWithVoucher.getTotalPrice();
        double expectedTotalPrice = (50.0 * 2 + 30.0 * 3) * (1 - 0.10); // 10% discount
        assertEquals(expectedTotalPrice, totalPrice, 0.001);
    }

    @Test
    public void testGetDecorator() {
        OrderWithVoucherDecorator decorator = orderWithVoucher.getDecorator();
        assertNotNull(decorator);
        assertEquals(orderWithVoucher.getId(), decorator.getId());
        assertEquals(voucher, decorator.getVoucher());
    }

    @Test
    public void testGetVoucher() {
        assertEquals(voucher, orderWithVoucher.getVoucher());
    }

    @Test
    public void testOrderWithVoucherConstructor() {
        OrderTemplate ordinaryOrder = new OrdinaryOrderEntity();
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        customerDetails.setFullname("John Doe");
        customerDetails.setUsername("johndoe");
        customerDetails.setUserId("user123");
        customerDetails.setPhoneNumber("123456789");
        customerDetails.setEmail("johndoe@example.com");

        ordinaryOrder.setUserId("user123");
        ordinaryOrder.setTimestamp("2023-05-20T15:30:00");
        ordinaryOrder.setShippingMethod("Fast");
        ordinaryOrder.setResi("1234567890");
        ordinaryOrder.setCustomerDetails(customerDetails);
        ordinaryOrder.setAddress("123 Test St");
        ordinaryOrder.setPrice("100");

        OrderWithVoucherEntity newOrderWithVoucher = new OrderWithVoucherEntity(ordinaryOrder, voucher);

        assertEquals("user123", newOrderWithVoucher.getUserId());
        assertEquals("2023-05-20T15:30:00", newOrderWithVoucher.getTimestamp());
        assertEquals("Fast", newOrderWithVoucher.getShippingMethod());
        assertEquals("1234567890", newOrderWithVoucher.getResi());
        assertEquals(customerDetails, newOrderWithVoucher.getCustomerDetails());
        assertEquals("123 Test St", newOrderWithVoucher.getAddress());
        assertEquals("100", newOrderWithVoucher.getPrice());
        assertEquals(voucher, newOrderWithVoucher.getVoucher());
    }

    @Test
    public void testSetData() {
        order2.setData(order1);

        assertEquals(order1.getId(), order2.getId());
        assertEquals(order1.getUserId(), order2.getUserId());
        assertEquals(order1.getTimestamp(), order2.getTimestamp());
        assertEquals(order1.getShippingMethod(), order2.getShippingMethod());
        assertEquals(order1.getResi(), order2.getResi());
        assertEquals(order1.getCustomerDetails().getFullname(), order2.getCustomerDetails().getFullname());
        assertEquals(order1.getCustomerDetails().getUsername(), order2.getCustomerDetails().getUsername());
        assertEquals(order1.getCustomerDetails().getUserId(), order2.getCustomerDetails().getUserId());
        assertEquals(order1.getCustomerDetails().getPhoneNumber(), order2.getCustomerDetails().getPhoneNumber());
        assertEquals(order1.getCustomerDetails().getEmail(), order2.getCustomerDetails().getEmail());
        assertEquals(order1.getAddress(), order2.getAddress());
        assertEquals(order1.getPrice(), order2.getPrice());
        assertEquals(order1.getCartItems().size(), order2.getCartItems().size());
        assertNotNull(order2.getCartItems().get(0).getOrder());
        assertEquals(order2, order2.getCartItems().get(0).getOrder());
    }

    @Test
    public void testSetStrategy() {
        CartPricingStrategy newStrategy = new CartPricingStrategy();
        order1.setStrategy(newStrategy);

        assertEquals(newStrategy, order1.getStrategy());
        assertEquals(newStrategy.calculateTotalPrice(order1.getCartItems()), order1.getTotalPrice(), 0.001);
    }

    @Test
    public void testOrdinaryPurchaseRequestTotalPrice() {
        // Create a sample cart with items
        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(new CartItemEntity(1L, "prod1", "Product 1", 1, 10.0));
        cartItems.add(new CartItemEntity(2L, "prod2", "Product 2", 1, 20.0));

        // Create an ordinary purchase request
        OrdinaryOrderEntity request = new OrdinaryOrderEntity();
        request.setCartItems(cartItems);

        // Check if the total price is calculated correctly
        assertEquals(30.0, request.getTotalPrice());
    }

    @Test
    public void testPurchaseRequestWithVoucherTotalPrice() {
        // Create a sample cart with items
        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(new CartItemEntity(1L, "prod1", "Product 1", 1, 10.0));
        cartItems.add(new CartItemEntity(2L, "prod2", "Product 2", 1, 20.0));

        // Create an ordinary purchase request
        OrdinaryOrderEntity ordinaryRequest = new OrdinaryOrderEntity();
        ordinaryRequest.setCartItems(cartItems);

        // Create a voucher
        DTOVoucher voucher = new DTOVoucher();
        voucher.setVoucherDiscount(0.1); // 10% discount

        // Create a purchase request with voucher
        OrderWithVoucherDecorator requestWithVoucher = new OrderWithVoucherDecorator(ordinaryRequest, voucher);

        // Check if the total price is calculated correctly with the voucher applied
        assertEquals(27.0, requestWithVoucher.getTotalPrice());
    }
}

