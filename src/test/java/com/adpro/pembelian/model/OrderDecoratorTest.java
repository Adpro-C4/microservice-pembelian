package com.adpro.pembelian.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adpro.pembelian.model.builder.CartItemBuilder;
import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class OrderDecoratorTest {

    private OrderTemplate order;
    private DTOVoucher voucher;
    private OrderWithVoucherDecorator decorator;

    @BeforeEach
    void setUp() {
        order = new OrdinaryOrderEntity();
        order.setPrice("100");
        CartItemEntity item = new CartItemBuilder().withId(12L).withQuantity(1).withPrice(100).build();
        ArrayList<CartItemEntity> cartItems = new  ArrayList<>();
        cartItems.add(item);
        order.setCartItems(cartItems);
        voucher = new DTOVoucher();
        voucher.setVoucherDiscount(0.1);
        decorator = new OrderWithVoucherDecorator();
        decorator = new OrderWithVoucherDecorator(order, voucher);
        decorator.setPrice("100");
    }

    @Test
    void getUserId_ReturnsCorrectUserId() {
        order.setUserId("testUserId");
        assertEquals("testUserId", decorator.getUserId());
    }

    @Test
    void getTotalPrice_WithVoucher_ReturnsCorrectTotalPrice() {
        double expectedTotalPrice = 90.0; // 100 - (100 * 0.1)
        assertEquals(expectedTotalPrice, decorator.getTotalPrice());
    }

    @Test
    void getTimestamp_ReturnsCorrectTimestamp() {
        order.setTimestamp("2024-05-21");
        assertEquals("2024-05-21", decorator.getTimestamp());
    }

    @Test
    void getShippingMethod_ReturnsCorrectShippingMethod() {
        order.setShippingMethod("Express");
        assertEquals("Express", decorator.getShippingMethod());
    }

    @Test
    void getResi_ReturnsCorrectResi() {
        order.setResi("123456");
        assertEquals("123456", decorator.getResi());
    }

    @Test
    void getCustomerDetails_ReturnsCorrectCustomerDetails() {
        DTOCustomerDetails customerDetails = new DTOCustomerDetails();
        customerDetails.setFullname("John Doe");
        order.setCustomerDetails(customerDetails);
        assertEquals(customerDetails, decorator.getCustomerDetails());
    }

    @Test
    void getAddress_ReturnsCorrectAddress() {
        order.setAddress("123 Main St");
        assertEquals("123 Main St", decorator.getAddress());
    }

}
