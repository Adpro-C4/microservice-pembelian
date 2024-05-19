package com.adpro.pembelian.model;

import org.junit.jupiter.api.Test;

import com.adpro.pembelian.model.dto.DTOTrackingOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOTrackingOrderTest {
    Long trackingId = 123L;
    String orderId = "ORD123";
    String method = "JTE";
    String resiCode = "JTE-1234567890";

    @Test
    void testDTOTrackingOrderBuilder() {
        

        DTOTrackingOrder dtoTrackingOrder = DTOTrackingOrder.builder()
                .trackingId(trackingId)
                .orderId(orderId)
                .methode(method)
                .resiCode(resiCode)
                .build();
        dtoTrackingOrder.setMethode(method);
        dtoTrackingOrder.setOrderId(orderId);
        dtoTrackingOrder.setResiCode(resiCode);
        dtoTrackingOrder.setTrackingId(trackingId);
        assertEquals(DTOTrackingOrder.builder().toString(), DTOTrackingOrder.builder().toString());

        assertEquals(trackingId, dtoTrackingOrder.getTrackingId());
        assertEquals(orderId, dtoTrackingOrder.getOrderId());
        assertEquals(method, dtoTrackingOrder.getMethode());
        assertEquals(resiCode, dtoTrackingOrder.getResiCode());
    }

    @Test
    void testDTOTrackingOrderToString() {
        Long trackingId = 123L;
        String orderId = "ORD123";
        String method = "JTE";
        String resiCode = "JTE-1234567890";

        DTOTrackingOrder dtoTrackingOrder = DTOTrackingOrder.builder()
                .trackingId(trackingId)
                .orderId(orderId)
                .methode(method)
                .resiCode(resiCode)
                .build();

        String expectedToString = "DTOTrackingOrder{" +
                "trackingId=" + trackingId +
                ", orderId='" + orderId + '\'' +
                ", methode='" + method + '\'' +
                ", resiCode='" + resiCode + '\'' +
                '}';

        assertEquals(expectedToString, dtoTrackingOrder.toString());
    }
}
