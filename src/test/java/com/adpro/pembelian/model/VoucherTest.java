package com.adpro.pembelian.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VoucherTest {

    @Test
    public void testPermanentVoucherCreation() {
        // Arrange
        Long voucherId = 1L;
        String voucherName = "Permanent Voucher";
        String voucherDescription = "This is a permanent voucher";
        double voucherDiscount = 20.0;
        String voucherType = "Permanent"; // Permanen
        Integer voucherQuota = null; // Tidak terbatas

        // Act
        Voucher voucher = new VoucherBuilder()
                .voucherId(voucherId)
                .voucherName(voucherName)
                .voucherDescription(voucherDescription)
                .voucherDiscount(voucherDiscount)
                .voucherType(voucherType)
                .voucherQuota(voucherQuota)
                .build();

        // Assert
        assertNotNull(voucher);
        assertEquals(voucherId, voucher.getVoucherId());
        assertEquals(voucherName, voucher.getVoucherName());
        assertEquals(voucherDescription, voucher.getVoucherDescription());
        assertEquals(voucherDiscount, voucher.getVoucherDiscount());
        assertEquals(voucherType, voucher.getVoucherType());
        assertNull(voucher.getVoucherQuota());
    }

    @Test
    public void testLimitedVoucherCreation() {
        // Arrange
        Long voucherId = 2L;
        String voucherName = "Limited Voucher";
        String voucherDescription = "This is a limited voucher";
        double voucherDiscount = 15.0;
        String voucherType = "Limited"; // Terbatas
        Integer voucherQuota = 50;

        // Act
        Voucher voucher = new VoucherBuilder()
                .voucherId(voucherId)
                .voucherName(voucherName)
                .voucherDescription(voucherDescription)
                .voucherDiscount(voucherDiscount)
                .voucherType(voucherType)
                .voucherQuota(voucherQuota)
                .build();

        // Assert
        assertNotNull(voucher);
        assertEquals(voucherId, voucher.getVoucherId());
        assertEquals(voucherName, voucher.getVoucherName());
        assertEquals(voucherDescription, voucher.getVoucherDescription());
        assertEquals(voucherDiscount, voucher.getVoucherDiscount());
        assertEquals(voucherType, voucher.getVoucherType());
        assertEquals(voucherQuota, voucher.getVoucherQuota());
    }

    @Test
    public void testInvalidVoucherType() {
        // Arrange
        Long voucherId = 3L;
        String voucherName = "Invalid Voucher";
        String voucherDescription = "This is an invalid voucher";
        double voucherDiscount = 10.0;
        String voucherType = "Random"; // Tipe yang tidak valid
        Integer voucherQuota = 30;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new VoucherBuilder()
                    .voucherId(voucherId)
                    .voucherName(voucherName)
                    .voucherDescription(voucherDescription)
                    .voucherDiscount(voucherDiscount)
                    .voucherType(voucherType)
                    .voucherQuota(voucherQuota)
                    .build();
        });
    }
}

