package com.adpro.pembelian.model.dto;

import com.adpro.pembelian.model.entity.Order;

public record DTOCheckoutRequest(DTOCustomerDetails customerInformation,
                                 Order purchaseRequest,
                                 String address
                                 ) {
}
