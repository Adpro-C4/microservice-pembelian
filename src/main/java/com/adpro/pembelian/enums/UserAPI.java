package com.adpro.pembelian.enums;

public enum UserAPI {

    GET_USERDATA("https://api-gateway-specialitystore.up.railway.app/user-data-service/data/customer");
    private final   String url;
    UserAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
