package com.adpro.pembelian.enums;

public enum UserAPI {

    GET_USERDATA("https://specialitystorebackend.up.railway.app/data/customer");
    private final   String url;
    UserAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
