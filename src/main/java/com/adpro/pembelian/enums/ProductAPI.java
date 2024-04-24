package com.adpro.pembelian.enums;

public enum ProductAPI {
    ALL_PRODUCT("https://microservice-katalog-production.up.railway.app/product/all"),
    PRODUCT("https://microservice-katalog-production.up.railway.app/product");
    private final   String url;
    ProductAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
