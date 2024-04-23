package com.adpro.pembelian.model.builder;

import com.adpro.pembelian.model.dto.DTOProduct;

public class ProductBuilder {
    private String productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private double productPrice;
    private String productThumbnail;

    private  String brand;

    public ProductBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public  ProductBuilder withBrand(String brand){
        this.brand = brand;
        return  this;
    }

    public ProductBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductBuilder withProductDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public ProductBuilder withProductStock(int productStock) {
        this.productStock = productStock;
        return this;
    }

    public ProductBuilder withProductPrice(double productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public ProductBuilder withProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
        return this;
    }

    public DTOProduct build() {
        DTOProduct product = new DTOProduct();
        product.setProductId(productId);
        product.setProductDescription(productDescription);
        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setProductStock(productStock);
        product.setProductThumbnail(productThumbnail);
        product.setBrand(brand);
        return product;
    }
}
