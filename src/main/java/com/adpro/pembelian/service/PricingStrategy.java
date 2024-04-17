package com.adpro.pembelian.service;

import java.util.List;

public interface PricingStrategy<T>{
    double calculateTotalPrice(List<T> item);
}
