package com.adpro.pembelian.enums;

public enum StatusAPI {

    POST_CREATE_STATUS("https://microservice-status-production.up.railway.app/create");
    private final   String url;
    StatusAPI(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
