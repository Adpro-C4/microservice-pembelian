package com.adpro.pembelian.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DTOTrackingOrder {
    private Long trackingId;
    private String orderId;
    private String methode;
    private String resiCode;
}
