package com.adpro.pembelian.model.dto;

import lombok.Data;
import java.util.Objects;

@Data
public class DTOCartItem {
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private double price;

    // Implementing hashCode, equals, and toString methods using Lombok
    @Override
    public int hashCode() {
        return Objects.hash(id, productId, name, quantity, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOCartItem that = (DTOCartItem) o;
        return quantity == that.quantity &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return "DTOCartItem{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

