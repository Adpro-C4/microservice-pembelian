package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.common.ShippingUtility;
import com.adpro.pembelian.enums.ShippingMethod;
import com.adpro.pembelian.enums.StatusAPI;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrderWithVoucherEntity;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.model.dto.*;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

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
    
    @Async
@Override
public CompletableFuture<Void> createPurchaseRequest(DTOPurchaseInformation request) {
    validatePurchaseRequest(request);
    Instant timestamp = Instant.now();
    String iso8601Timestamp = timestamp.toString();

    CompletableFuture<DTOCustomerDetails> customerDetailsFuture = CompletableFuture.supplyAsync(() -> {
        return getCustomerDetails(request.userId());
    });
    CompletableFuture<String> resiFuture = CompletableFuture.supplyAsync(() -> {
        return ShippingUtility.generateTrackingCode(request.shippingMethod());
    });

    return customerDetailsFuture.thenCombine(resiFuture, (customerDetails, resi) -> {
        OrderTemplate orderRequest = buildOrderRequest(request, iso8601Timestamp, customerDetails, resi);
        CompletableFuture<Void> sendOrderStatusFuture = CompletableFuture.runAsync(() -> {
            sendOrderStatus(orderRequest);
        });
        CompletableFuture<Void> saveOrderFuture = CompletableFuture.runAsync(() -> {
            saveOrder(orderRequest);
        });
        return CompletableFuture.allOf(sendOrderStatusFuture, saveOrderFuture);
    }).thenCompose(result -> CompletableFuture.completedFuture(null));
}

    private void validatePurchaseRequest(DTOPurchaseInformation request) {
        if (request.cartItems().isEmpty() || request.cartItems() == null) {
            throw new IllegalArgumentException("order items tidak boleh kosong");
        }
        if (customerDetailsService.getUserDetailsAPI(request.userId()) == null) {
            throw new NoSuchElementException("User yang melakukan order tidak ditemukan");
        }
        if (!ShippingMethod.contains(request.shippingMethod())) {
            throw new IllegalArgumentException("Metode pengiriman tidak didukung");
        }
    }

    private DTOCustomerDetails getCustomerDetails(String userId) {
        return customerDetailsService.getUserDetailsAPI(userId);
    }

    private OrderTemplate buildOrderRequest(DTOPurchaseInformation request, String iso8601Timestamp, DTOCustomerDetails customerDetails, String resi) {
        List<CartItemEntity> cartItems = getCartItems(request.userId(), request.cartItems());
        OrderTemplate orderRequest = createOrderEntity(request, iso8601Timestamp, customerDetails, resi, cartItems);
        orderRequest.setPrice(String.valueOf(orderRequest.getStrategy().
                calculateTotalPrice(orderRequest.getCartItems())));
        return  request.voucherId() != null ? new OrderWithVoucherEntity(orderRequest, voucherService.getVoucher(request.voucherId())) : orderRequest;
    }

    private List<CartItemEntity> getCartItems(String userId, List<String> cartItemIds) {
        return cartService.getCartItemsFromShoppingCart(userId).stream()
                .filter(cartItem -> cartItemIds.contains(String.valueOf(cartItem.getId())))
                .toList();
    }
    private OrderTemplate createOrderEntity(DTOPurchaseInformation request, String iso8601Timestamp,
                                            DTOCustomerDetails customerDetails, String resi,
                                            List<CartItemEntity> cartItems) {
        OrdinaryOrderEntity ordinaryPurchaseRequest = new OrdinaryOrderEntity();
        ordinaryPurchaseRequest.setUserId(request.userId());
        ordinaryPurchaseRequest.setAddress(request.address());
        ordinaryPurchaseRequest.setTimestamp(iso8601Timestamp);
        ordinaryPurchaseRequest.setShippingMethod(request.shippingMethod());
        ordinaryPurchaseRequest.setResi(resi);
        ordinaryPurchaseRequest.setCustomerDetails(customerDetails);
        ordinaryPurchaseRequest.setCartItems(cartItems);
        return ordinaryPurchaseRequest;
    }

    private void sendOrderStatus(OrderTemplate orderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderRequest.getId());
        data.put("orderStatus", "Menunggu Konfirmasi");
        HttpEntity<Map<String,Object>> requestBody = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForEntity(StatusAPI.POST_CREATE_STATUS.getUrl(), requestBody, String.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void saveOrder(OrderTemplate orderRequest) {
        orderRepository.save(orderRequest);
    }

    @Override
    public void removePurchaseRequest(String orderId) {
        OrderTemplate order = viewOrder(orderId);
        if(order == null){
            throw new NoSuchElementException("Order tidak ditemukan");
        }
        orderRepository.deleteById(Long.parseLong(orderId));
    }

    @Override
    public OrderTemplate viewOrder(String orderId) {
        OrderTemplate order = orderRepository.findById(Long.parseLong(orderId)).orElse(null);
        if(order == null){
            throw new NoSuchElementException("order tidak ditemukan");
        }
        return order;
    }

    @Override
    public List<OrderTemplate> viewAllOrderByUserId(String userId) {
        if(customerDetailsService.getUserDetailsAPI(userId) == null){
            throw new NoSuchElementException("user sudah dihapus atau tidak ditemukan");
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void removePurchaseRequestByUserId(String userId) {
        orderRepository.deleteByUserId(userId);
    }

}
