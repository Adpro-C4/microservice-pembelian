package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.entity.decorator.OrdinaryOrder;
import com.adpro.pembelian.model.entity.decorator.OrderWithVoucher;
import com.adpro.pembelian.model.dto.*;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class PurchaseServiceImpl implements  PurchaseService {
    @Autowired
    APICustomerDetailsService customerDetailsService;
    @Autowired
    APIVoucherService voucherService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CartService cartService;
    @Override
    public void createPurchaseRequest(DTOPurchaseInformation request) {
        DTOVoucher voucher = null;
        Instant timestamp = Instant.now();
        String iso8601Timestamp = timestamp.toString();
        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(request.userId());
        Order purchaseRequest = null;
        OrdinaryOrder ordinaryPurchaseRequest = new OrdinaryOrder();
        ordinaryPurchaseRequest.setUserId(request.userId());
        ordinaryPurchaseRequest.setAddress(request.address());
        ordinaryPurchaseRequest.setTimestamp(iso8601Timestamp);
        ordinaryPurchaseRequest.setCustomerDetails(customerDetails);
        ordinaryPurchaseRequest.setCartItems(cartService.getCartItemsFromShoppingCart(
                request.userId()).stream().filter(cartItem ->
                request.cartItems().contains(String.valueOf(cartItem.getId()))).toList());
        ordinaryPurchaseRequest.setPrice(
                String.valueOf(ordinaryPurchaseRequest.getStrategy().
                        calculateTotalPrice(ordinaryPurchaseRequest.getCartItems())));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> response = null;
        if(request.voucherId() != null){
            voucher = voucherService.getVoucher(request.voucherId());
            purchaseRequest = new OrderWithVoucher( ordinaryPurchaseRequest, voucher);
            purchaseRequest.setPrice(String.valueOf(purchaseRequest.getTotalPrice()));
            response = new HttpEntity<>(purchaseRequest, headers);
            restTemplate.postForEntity(, response, String.class);
        }
        else{
            restTemplate.pos
        }

    }


}
