package com.adpro.pembelian.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
    private String productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private  double productPrice;
    private String productThumbnail;

    public ProductDTO(String productId, String productName, String productDescription, int productStock,
                      double productPrice, String productThumbnail){
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productThumbnail = productThumbnail;
        setProductStock(productStock);
        setProductPrice(productPrice);
    }

    public ProductDTO(){}

    public void setProductStock(int productStock) {
        validateStock(productStock);
        this.productStock = productStock;
    }
    public void setProductPrice(double productPrice) {
        validatePrice(productPrice);
        this.productPrice = productPrice;
    }
    private void validateStock(int productStock) {
        if (productStock < 0) {
            throw new IllegalArgumentException("Stok produk tidak boleh kurang dari 0");
        }
    }
    private void validatePrice(double productPrice) {
        if (productPrice <= 0) {
            throw new IllegalArgumentException("Harga produk harus bilangan positif");
        }
    }
}
