package com.adpro.pembelian.enums;

public enum ProductAPI {
    ALL_PRODUCT("https://api-gateway-specialitystore.up.railway.app/product-service/product/all"),
    PRODUCT("https://api-gateway-specialitystore.up.railway.app/product-service/product");
    private final   String url;
    ProductAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
