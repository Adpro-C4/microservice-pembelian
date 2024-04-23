package com.adpro.pembelian.enums;

public enum ProductApi {
    ALL_PRODUCT("https://microservice-katalog-production.up.railway.app/product/all"),
    PRODUCT("https://microservice-katalog-production.up.railway.app/product");
    private final   String url;
    ProductApi(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
