package com.adpro.pembelian.enums;

public enum ProductAPI {
    ALL_PRODUCT("https://api-gateway-specialitystore.up.railway.app/product-service/product/all"),
    PRODUCT("https://api-gateway-specialitystore.up.railway.app/product-service/product"),
    VOUCHER_BY_ID("https://api-gateway-specialitystore.up.railway.app/voucher-service/voucher/id");
    private final   String url;
    ProductAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
