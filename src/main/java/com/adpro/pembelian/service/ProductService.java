package com.adpro.pembelian.service;

import com.adpro.pembelian.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductFromAPI(String id);
    List<Product> getAllProductFromAPI();
}
