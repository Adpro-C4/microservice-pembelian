package com.adpro.pembelian.model;

import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.decorator.OrdinaryPurchaseRequest;
import com.adpro.pembelian.model.decorator.PurchaseRequestWithVoucher;
import com.adpro.pembelian.model.entity.CartItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestTest {

    @Test
    public void testOrdinaryPurchaseRequestTotalPrice() {
        // Create a sample cart with items
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "prod1", "Product 1", 1, 10.0));
        cartItems.add(new CartItem(2L, "prod2", "Product 2", 1, 20.0));

        // Create an ordinary purchase request
        OrdinaryPurchaseRequest request = new OrdinaryPurchaseRequest();
        request.setCartItems(cartItems);

        // Check if the total price is calculated correctly
        assertEquals(30.0, request.getTotalPrice());
    }

    @Test
    public void testPurchaseRequestWithVoucherTotalPrice() {
        // Create a sample cart with items
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "prod1", "Product 1", 1, 10.0));
        cartItems.add(new CartItem(2L, "prod2", "Product 2", 1, 20.0));

        // Create an ordinary purchase request
        OrdinaryPurchaseRequest ordinaryRequest = new OrdinaryPurchaseRequest();
        ordinaryRequest.setCartItems(cartItems);

        // Create a voucher
        DTOVoucher voucher = new DTOVoucher();
        voucher.setVoucherDiscount(0.1); // 10% discount

        // Create a purchase request with voucher
        PurchaseRequestWithVoucher requestWithVoucher = new PurchaseRequestWithVoucher(ordinaryRequest, voucher);

        // Check if the total price is calculated correctly with the voucher applied
        assertEquals(27.0, requestWithVoucher.getTotalPrice());
    }
}

