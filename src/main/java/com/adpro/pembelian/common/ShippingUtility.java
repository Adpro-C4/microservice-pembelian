package com.adpro.pembelian.common;

import java.util.Random;

public class ShippingUtility {

    public static String generateTrackingCode(String shippingMethod) {
        switch (shippingMethod) {
            case "JTE":
                return generateJTETrackingCode();
            case "Go-bek":
                return generateGoBekTrackingCode();
            case "SiWuzz":
                return generateSiWuzzTrackingCode();
            default:
                throw new IllegalArgumentException("Metode pengiriman tidak valid: " + shippingMethod);
        }
    }

    private static String generateJTETrackingCode() {
        // Generate JTE tracking code: "JTE-" + 12-digit number
        return "JTE-" + generateRandomNumber(12);
    }

    private static String generateGoBekTrackingCode() {
        // Generate Go-bek tracking code: "GBK-" + 8 uppercase letters + 4 digits
        return "GBK-" + generateRandomUpperCaseLetters(8) + generateRandomDigits(4);
    }

    private static String generateSiWuzzTrackingCode() {
        // Generate SiWuzz tracking code: "SWZ-" + 16 uppercase letters
        return "SWZ-" + generateRandomUpperCaseLetters(16);
    }

    private static String generateRandomNumber(int length) {
        // Generate random number string with specified length
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static String generateRandomUpperCaseLetters(int length) {
        // Generate random uppercase letters string with specified length
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static String generateRandomDigits(int length) {
        // Generate random digits string with specified length
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
