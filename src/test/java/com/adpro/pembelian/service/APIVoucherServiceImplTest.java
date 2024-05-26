package com.adpro.pembelian.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.adpro.pembelian.enums.ProductAPI;
import com.adpro.pembelian.model.dto.DTOVoucher;
import com.adpro.pembelian.service.external.APIVoucherServiceImpl;

class APIVoucherServiceImplTest {

    @Mock
    private DTOVoucher mockVoucher;

    @Mock
    private RestTemplate restTemplate;

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
        // Configure mock RestTemplate
        when(restTemplate.getForObject(ProductAPI.VOUCHER_BY_ID.getUrl() + "?id=" + TEST_VOUCHER_ID, DTOVoucher.class))
            .thenReturn(mockVoucher);

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
        when(restTemplate.getForObject(ProductAPI.VOUCHER_BY_ID.getUrl() + "?id=" + TEST_VOUCHER_ID, DTOVoucher.class))
            .thenReturn(null);
        DTOVoucher result = voucherService.getVoucher(TEST_VOUCHER_ID);
        assertNull(result);
    }
}

