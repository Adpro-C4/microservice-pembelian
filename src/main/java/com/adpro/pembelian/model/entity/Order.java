package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.service.internal.PricingStrategy;
import jakarta.persistence.Entity;

import java.util.List;

public abstract class Order {

    protected String userId;
    protected   String timestamp;
    protected DTOCustomerDetails customerDetails;
    protected String address;
    protected List<CartItem> cartItems;
    protected PricingStrategy<CartItem> strategy;
    protected String price;

    public abstract String getUserId();
    public abstract String getTimestamp();

    public abstract void setPrice(String price);
    public abstract DTOCustomerDetails getCustomerDetails();
    public abstract String getAddress();
    public abstract List<CartItem> getCartItems();
    public abstract double getTotalPrice();
}
