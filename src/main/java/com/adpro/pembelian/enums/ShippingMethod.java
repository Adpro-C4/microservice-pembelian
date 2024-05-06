package com.adpro.pembelian.enums;

public enum ShippingMethod {
    JTE("JTE"),
    GO_BEK("Go-bek"),
    SI_WUZZ("SiWuzz");

    private final String method;

    ShippingMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static boolean contains(String value) {
        for (ShippingMethod method : ShippingMethod.values()) {
            if (method.getMethod().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
