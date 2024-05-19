package com.adpro.pembelian.service.internal;

import com.adpro.pembelian.common.ShippingUtility;
import com.adpro.pembelian.enums.ShippingMethod;
import com.adpro.pembelian.eventdriven.RabbitMQProducer;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrderWithVoucherEntity;
import com.adpro.pembelian.model.entity.OrdinaryOrderEntity;
import com.adpro.pembelian.model.dto.*;
import com.adpro.pembelian.repository.OrderRepository;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.external.APIVoucherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
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
    @Autowired
    RabbitMQProducer rabbit;
    
    @Async
    @Transactional
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

        customerDetailsFuture.join();
        String resi = resiFuture.join();

        System.out.println("ini resi kamu: " + resi);
        OrderTemplate orderRequest = buildOrderRequest(request, iso8601Timestamp, customerDetailsFuture.join(), resi);
        System.out.println("order request: " + orderRequest);
        System.out.println("HINDARI DEADLOCK");
        CompletableFuture.runAsync(() -> {
            sendTrackingOrder(orderRequest);
        });

        CompletableFuture.runAsync(() -> {
            saveOrder(orderRequest);
        });
        return  null;
    }


    private void validatePurchaseRequest(DTOPurchaseInformation request) {
        if (request.cartItems() == null || request.cartItems().isEmpty()) {
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
        System.out.println("cart items: "+ cartItems.size());
        OrderTemplate orderRequest = createOrderEntity(request, iso8601Timestamp, customerDetails, resi, cartItems);
        orderRequest.setPrice(String.valueOf(orderRequest.getStrategy().
                calculateTotalPrice(orderRequest.getCartItems())));
        return  request.voucherId() != null ? new OrderWithVoucherEntity(orderRequest, voucherService.getVoucher(request.voucherId())) : orderRequest;
    }

    private List<CartItemEntity> getCartItems(String userId, List<String> cartItemIds) {
        List<CartItemEntity> cartItemEntities = cartService.getCartItemsFromShoppingCart(userId);
        System.out.println("Cart Items dari service:");
        cartItemEntities.forEach(cartItem -> System.out.println(cartItem.getProductId()));
        System.out.println("Cart Items dari cartItemIds:");
        cartItemIds.forEach(System.out::println);
        return cartItemEntities.stream()
                .filter(cartItem -> cartItemIds.contains(String.valueOf(cartItem.getProductId())))
                .toList();
    }




    private OrderTemplate createOrderEntity(DTOPurchaseInformation request, String iso8601Timestamp,
                                            DTOCustomerDetails customerDetails, String resi,
                                            List<CartItemEntity> cartItems) {
        OrdinaryOrderEntity ordinaryPurchaseRequest = new OrdinaryOrderEntity();
        ordinaryPurchaseRequest.setId(UUID.randomUUID().toString());
        ordinaryPurchaseRequest.setUserId(request.userId());
        ordinaryPurchaseRequest.setAddress(request.address());
        ordinaryPurchaseRequest.setTimestamp(iso8601Timestamp);
        ordinaryPurchaseRequest.setShippingMethod(request.shippingMethod());
        ordinaryPurchaseRequest.setResi(resi);
        ordinaryPurchaseRequest.setCustomerDetails(customerDetails);
        ordinaryPurchaseRequest.setCartItems(cartItems);
        cartItems.forEach(cartItemEntity -> cartItemEntity.setOrder(ordinaryPurchaseRequest));
        return ordinaryPurchaseRequest;
    }

  

    private void sendTrackingOrder(OrderTemplate orderRequest){
        DTOTrackingOrder trackingOrder = DTOTrackingOrder.builder()
        .orderId(orderRequest.getId())
        .methode(orderRequest.getShippingMethod())
        .resiCode("auto")
        .build();
        try {
            rabbit.sendMessage("tracking-order-routing-key", new ObjectMapper().writeValueAsString(trackingOrder));
            System.out.println("HEHEHE");
        } catch (JsonProcessingException e) {
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
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderTemplate viewOrder(String orderId) {
        OrderTemplate order = orderRepository.findById(orderId).orElse(null);
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
