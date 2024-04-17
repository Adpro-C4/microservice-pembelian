package com.adpro.pembelian.service;

public interface PricingStrategy<T>{
    double calculateTotalPrice(T item);
}
