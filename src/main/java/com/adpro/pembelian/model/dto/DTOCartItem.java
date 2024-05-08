package com.adpro.pembelian.model.dto;

import lombok.Data;

@Data
public class DTOCartItem {
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private double price;


}
