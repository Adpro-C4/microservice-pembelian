package com.adpro.pembelian.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private  double productPrice;
    private String productThumbnail;

    public void setProductStock(int productStock) {
        if(productStock < 0){
            throw new IllegalArgumentException("Stok produk tidak boleh kurang dari 0");
        }
        this.productStock = productStock;
    }

    public void setProductPrice(double productPrice) {
        if(productPrice <= 0){
            throw new IllegalArgumentException("Harga produk harus bilangan positif");
        }
        this.productPrice = productPrice;
    }
}
