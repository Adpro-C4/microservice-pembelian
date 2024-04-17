package com.adpro.pembelian.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class ProductDTOTest {

    @Test
    public void testProductGetterSetter() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        String productId = "123";
        String productName = "Test Product";
        String productDescription = "This is a test product";
        int productStock = 10;
        double productPrice = 99.99;
        String productThumbnail = "test_thumbnail.jpg";

        // Act
        productDTO.setProductId(productId);
        productDTO.setProductName(productName);
        productDTO.setProductDescription(productDescription);
        productDTO.setProductStock(productStock);
        productDTO.setProductPrice(productPrice);
        productDTO.setProductThumbnail(productThumbnail);

        // Assert
        assertEquals(productId, productDTO.getProductId());
        assertEquals(productName, productDTO.getProductName());
        assertEquals(productDescription, productDTO.getProductDescription());
        assertEquals(productStock, productDTO.getProductStock());
        assertEquals(productPrice, productDTO.getProductPrice(), 0.01);
        assertEquals(productThumbnail, productDTO.getProductThumbnail());
    }

    @Test
    public void testProductStockZero() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        int productStock = 0;
        // Act
        productDTO.setProductStock(productStock);
        // Assert
        assertEquals(productStock, productDTO.getProductStock());
    }

    @Test
    public void testProductStockNegative() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        int productStock = -10;
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productDTO.setProductStock(productStock);
        });
    }

    @Test
    public void testProductStockPositive() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        int productStock = 50;
        // Act
        productDTO.setProductStock(productStock);
        // Assert
        assertEquals(productStock, productDTO.getProductStock());
    }

    @Test
    public void testProductPricePositive() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        double productPrice = 99.99;

        // Act
        productDTO.setProductPrice(productPrice);

        // Assert
        assertEquals(productPrice, productDTO.getProductPrice(), 0.01);
    }

    @Test
    public void testProductPriceNegative() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        double productPrice = -10.50;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productDTO.setProductPrice(productPrice);
        });
    }
    @Test
    public void testProductPriceZero() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        double productPrice = 0.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productDTO.setProductPrice(productPrice);
        });
    }
}

