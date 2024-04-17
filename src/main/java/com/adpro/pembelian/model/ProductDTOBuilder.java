package com.adpro.pembelian.model;

public class ProductDTOBuilder {
    private String productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private double productPrice;
    private String productThumbnail;

    public ProductDTOBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public ProductDTOBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductDTOBuilder withProductDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public ProductDTOBuilder withProductStock(int productStock) {
        this.productStock = productStock;
        return this;
    }

    public ProductDTOBuilder withProductPrice(double productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public ProductDTOBuilder withProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
        return this;
    }

    public ProductDTO build() {
        return new ProductDTO(productId, productName, productDescription, productStock, productPrice, productThumbnail);
    }
}
