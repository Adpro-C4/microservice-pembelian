package com.adpro.pembelian.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.adpro.pembelian.model.dto.DTOProduct;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void testProductGetterSetter() {
        // Arrange
        DTOProduct product = new DTOProduct();
        String productId = "123";
        String productName = "Test Product";
        String productDescription = "This is a test product";
        int productStock = 10;
        double productPrice = 99.99;
        String productThumbnail = "test_thumbnail.jpg";

        // Act
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductStock(productStock);
        product.setProductPrice(productPrice);
        product.setProductThumbnail(productThumbnail);

        // Assert
        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
        assertEquals(productDescription, product.getProductDescription());
        assertEquals(productStock, product.getProductStock());
        assertEquals(productPrice, product.getProductPrice(), 0.01);
        assertEquals(productThumbnail, product.getProductThumbnail());
    }

    @Test
    void testProductStockZero() {
        // Arrange
        DTOProduct product = new DTOProduct();
        int productStock = 0;
        // Act
        product.setProductStock(productStock);
        // Assert
        assertEquals(productStock, product.getProductStock());
    }

    @Test
    void testProductStockNegative() {
        // Arrange
        DTOProduct product = new DTOProduct();
        int productStock = -10;
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductStock(productStock);
        });
    }

    @Test
    void testProductStockPositive() {
        // Arrange
        DTOProduct product = new DTOProduct();
        int productStock = 50;
        // Act
        product.setProductStock(productStock);
        // Assert
        assertEquals(productStock, product.getProductStock());
    }

    @Test
    void testProductPricePositive() {
        // Arrange
        DTOProduct product = new DTOProduct();
        double productPrice = 99.99;

        // Act
        product.setProductPrice(productPrice);

        // Assert
        assertEquals(productPrice, product.getProductPrice(), 0.01);
    }

    @Test
    void testProductPriceNegative() {
        // Arrange
        DTOProduct product = new DTOProduct();
        double productPrice = -10.50;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductPrice(productPrice);
        });
    }

}

