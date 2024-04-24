package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.common.ShippingUtility;
import com.adpro.pembelian.enums.StatusAPI;
import com.adpro.pembelian.model.entity.Order;
import com.adpro.pembelian.model.entity.decorator.OrdinaryOrder;
import com.adpro.pembelian.model.entity.decorator.OrderWithVoucher;
import com.adpro.pembelian.model.dto.*;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String resi = ShippingUtility.generateTrackingCode(request.shippingMethod());
        OrdinaryOrder ordinaryPurchaseRequest = new OrdinaryOrder();
        ordinaryPurchaseRequest.setUserId(request.userId());
        ordinaryPurchaseRequest.setAddress(request.address());
        ordinaryPurchaseRequest.setTimestamp(iso8601Timestamp);
        ordinaryPurchaseRequest.setShippingMethod(request.shippingMethod());
        ordinaryPurchaseRequest.setResi(resi);
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderRequest.getId());
        data.put("orderStatus", "Menunggu Konfirmasi");

        orderRequest.setShippingMethod(request.shippingMethod());
        orderRequest.setResi(resi);

        HttpEntity<Map<String,Object>> requestBody = new HttpEntity<>(data, headers);
        try{
            restTemplate.postForEntity(StatusAPI.POST_CREATE_STATUS.getUrl(), requestBody,String.class);
        }
        catch (Exception e){
            System.out.println(e);
        }
        orderRepository.save(orderRequest);
    }

    @Override
    public void removePurchaseRequest(String orderId) {
        orderRepository.deleteById(Long.parseLong(orderId));
    }

    @Override
    public Order viewOrder(String orderId) {
        return orderRepository.findById(Long.parseLong(orderId)).orElse(null);
    }

    @Override
    public List<Order> viewAllOrderByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

}
