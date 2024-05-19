package com.adpro.pembelian.model;

import org.junit.jupiter.api.Test;

import com.adpro.pembelian.model.dto.DTOProduct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DTOProductTest {

    @Test
    void testParameterizedConstructorAndGetters() {
        // Arrange
        String productId = "P123";
        String productName = "Product A";
        String productDescription = "Description A";
        int productStock = 10;
        double productPrice = 20.5;
        String productThumbnail = "thumbnail.jpg";
        String brand = "Brand A";

        // Act
        DTOProduct product = new DTOProduct(productId, productName, productDescription, productStock,
                productPrice, productThumbnail, brand);

        // Assert
        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
        assertEquals(productDescription, product.getProductDescription());
        assertEquals(productStock, product.getProductStock());
        assertEquals(productPrice, product.getProductPrice());
        assertEquals(productThumbnail, product.getProductThumbnail());
        assertEquals(brand, product.getBrand());
    }

    @Test
    void testSetProductStock() {
        // Arrange
        int productStock = -5;
        DTOProduct product = new DTOProduct();

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setProductStock(productStock);
        });
        assertEquals("Stok produk tidak boleh kurang dari 0", exception.getMessage());
    }

    @Test
    void testSetProductPrice() {
        // Arrange
        double productPrice = -10.5;
        DTOProduct product = new DTOProduct();

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setProductPrice(productPrice);
        });
        assertEquals("Harga produk harus bilangan positif", exception.getMessage());
    }
}
