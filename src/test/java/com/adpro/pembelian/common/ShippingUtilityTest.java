package com.adpro.pembelian.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class ShippingUtilityTest {

    @BeforeEach
    void before(){
        new ShippingUtility();
    }

    @Test
    void testGenerateTrackingCodeJTE() {
        String trackingCode = ShippingUtility.generateTrackingCode("JTE");
        assertTrue(trackingCode.startsWith("JTE-"));
        assertEquals(16, trackingCode.length()); 
    }

    @Test
    void testGenerateTrackingCodeGoBek() {
        String trackingCode = ShippingUtility.generateTrackingCode("Go-bek");
        assertTrue(trackingCode.startsWith("GBK-"));
        assertEquals(16, trackingCode.length());
    }

    @Test
    void testGenerateTrackingCodeSiWuzz() {
        String trackingCode = ShippingUtility.generateTrackingCode("SiWuzz");
        assertTrue(trackingCode.startsWith("SWZ-"));
        assertEquals(20, trackingCode.length()); 
    }

    @Test
    void testGenerateTrackingCodeInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> ShippingUtility.generateTrackingCode("Express"));
    }
}

