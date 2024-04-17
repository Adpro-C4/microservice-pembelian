package com.adpro.pembelian.model;

public class ProductBuilder {
    private String productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private double productPrice;
    private String productThumbnail;

    public ProductBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
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

    public Product build() {
        return new Product(productId, productName, productDescription, productStock, productPrice, productThumbnail);
    }
}
