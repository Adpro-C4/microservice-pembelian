package com.adpro.pembelian.service.internal;

import java.util.List;

public interface PricingStrategy<T>{
    double calculateTotalPrice(List<T> item);
}
