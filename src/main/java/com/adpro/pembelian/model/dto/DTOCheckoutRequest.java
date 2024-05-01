package com.adpro.pembelian.model.dto;

import com.adpro.pembelian.model.entity.OrderTemplate;

public record DTOCheckoutRequest(DTOCustomerDetails customerInformation,
                                 OrderTemplate purchaseRequest,
                                 String address
                                 ) {
}
