package com.adpro.pembelian.model.dto;

public record DTOCheckoutRequest(DTOCustomerDetails customerInformation,
                                 DTOShoppingCartInformation shoppingCartInformation,
                                 DTOVoucher voucherInformation,
                                 String address
                                 ) {
}
