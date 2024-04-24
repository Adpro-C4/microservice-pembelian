package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.entity.decorator.OrdinaryOrder;
import com.adpro.pembelian.model.entity.decorator.OrderWithVoucher;
import com.adpro.pembelian.model.dto.*;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
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
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;
    @Override
    public void createPurchaseRequest(DTOPurchaseInformation request) {
        DTOVoucher voucher = null;
        Instant timestamp = Instant.now();
        String iso8601Timestamp = timestamp.toString();
        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(request.userId());
        Order orderRequest = null;
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

        if (request.voucherId() != null) {
            voucher = voucherService.getVoucher(request.voucherId());
            orderRequest = new OrderWithVoucher(ordinaryPurchaseRequest, voucher);
        } else {
            orderRequest = ordinaryPurchaseRequest;
        }
        orderRepository.save(orderRequest);

    }

}
