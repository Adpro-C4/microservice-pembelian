package com.adpro.pembelian.service;

import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.service.internal.VoucherCartPricingStrategy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VoucherCartPricingStrategyTest {

   

    @Test
    void testCalculateTotalPriceWithVoucher() {
        // Arrange
        DTOVoucher voucher = new DTOVoucher();
        voucher.setVoucherDiscount(0.1); // 10% discount
        VoucherCartPricingStrategy pricingStrategy = new VoucherCartPricingStrategy();
        pricingStrategy.setVoucher(voucher);
        List<CartItemEntity> items = new ArrayList<>();
        CartItemEntity item1 = mock(CartItemEntity.class);
        CartItemEntity item2 = mock(CartItemEntity.class);
        items.add(item1);
        items.add(item2);

        when(item1.calculateTotalPrice()).thenReturn(50.0);
        when(item2.calculateTotalPrice()).thenReturn(70.0);

        // Act
        double totalPrice = pricingStrategy.calculateTotalPrice(items);

        // Assert
        assertEquals(108.0, totalPrice); // Total price after discount
    }
}

