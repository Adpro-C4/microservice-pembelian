package com.adpro.pembelian.enums;

public enum VoucherType {
    LIMITED("Limited"),
    PERMANENT("Permanent");

    private final String type;

    VoucherType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isValidType(String text) {
        for (VoucherType type : VoucherType.values()) {
            if (type.type.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }
}

