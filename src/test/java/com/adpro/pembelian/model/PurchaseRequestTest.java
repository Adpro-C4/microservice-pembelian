package com.adpro.pembelian.model;

import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.model.decorator.OrderWithVoucherDecorator;
import com.adpro.pembelian.model.entity.CartItemEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestTest {

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

