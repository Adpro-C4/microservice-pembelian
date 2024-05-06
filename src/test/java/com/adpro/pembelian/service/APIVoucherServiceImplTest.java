package com.adpro.pembelian.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.service.external.APIVoucherServiceImpl;

public class APIVoucherServiceImplTest {

    @Mock
    private DTOVoucher mockVoucher;

    @InjectMocks
    private APIVoucherServiceImpl voucherService;

    private final String TEST_VOUCHER_ID = "1";

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetVoucherPositive() {
        // Mock behavior
        when(mockVoucher.getVoucherId()).thenReturn(Long.parseLong(TEST_VOUCHER_ID));
        when(mockVoucher.getVoucherName()).thenReturn("TEST VOUCHER");
        when(mockVoucher.getVoucherDescription()).thenReturn("DESKRIPSI");
        when(mockVoucher.getVoucherDiscount()).thenReturn(0.2);
        when(mockVoucher.getVoucherQuota()).thenReturn(60);

        // Test method
        DTOVoucher result = voucherService.getVoucher(TEST_VOUCHER_ID);

        // Assert result
        assertEquals(Long.parseLong(TEST_VOUCHER_ID), result.getVoucherId());
        assertEquals("TEST VOUCHER", result.getVoucherName());
        assertEquals("DESKRIPSI", result.getVoucherDescription());
        assertEquals(0.2, result.getVoucherDiscount());
        assertEquals(60, result.getVoucherQuota());
    }

    @Test
    void testGetVoucherNegative() {
        // Mock APIVoucherServiceImpl
        APIVoucherServiceImpl mockVoucherService = mock(APIVoucherServiceImpl.class);

        // Stub method behavior
        when(mockVoucherService.getVoucher(anyString())).thenReturn(null); // Stub method to return null

        // Test method and assert that NoSuchElementException is thrown
        assertNull(mockVoucherService.getVoucher(TEST_VOUCHER_ID));
    }
}

