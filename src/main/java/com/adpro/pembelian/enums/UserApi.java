package com.adpro.pembelian.enums;

public enum UserApi {

    GET_USERDATA("https://specialitystorebackend.up.railway.app/data/customer");
    private final   String url;
    UserApi(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
