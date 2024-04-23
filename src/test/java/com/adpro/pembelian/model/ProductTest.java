package com.adpro.pembelian.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void testProductGetterSetter() {
        // Arrange
        Product product = new Product();
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
    public void testProductStockZero() {
        // Arrange
        Product product = new Product();
        int productStock = 0;
        // Act
        product.setProductStock(productStock);
        // Assert
        assertEquals(productStock, product.getProductStock());
    }

    @Test
    public void testProductStockNegative() {
        // Arrange
        Product product = new Product();
        int productStock = -10;
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductStock(productStock);
        });
    }

    @Test
    public void testProductStockPositive() {
        // Arrange
        Product product = new Product();
        int productStock = 50;
        // Act
        product.setProductStock(productStock);
        // Assert
        assertEquals(productStock, product.getProductStock());
    }

    @Test
    public void testProductPricePositive() {
        // Arrange
        Product product = new Product();
        double productPrice = 99.99;

        // Act
        product.setProductPrice(productPrice);

        // Assert
        assertEquals(productPrice, product.getProductPrice(), 0.01);
    }

    @Test
    public void testProductPriceNegative() {
        // Arrange
        Product product = new Product();
        double productPrice = -10.50;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductPrice(productPrice);
        });
    }

}

