package com.adpro.pembelian.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDTOBuilderTest {

    @Test
    public void testProductBuilder() {
        // Arrange
        String productId = "123";
        String productName = "Test Product";
        String productDescription = "This is a test product";
        int productStock = 10;
        double productPrice = 99.99;
        String productThumbnail = "test_thumbnail.jpg";

        // Act
        ProductDTO productDTO = new ProductDTOBuilder()
                .withProductId(productId)
                .withProductName(productName)
                .withProductDescription(productDescription)
                .withProductStock(productStock)
                .withProductPrice(productPrice)
                .withProductThumbnail(productThumbnail)
                .build();

        // Assert
        assertEquals(productId, productDTO.getProductId());
        assertEquals(productName, productDTO.getProductName());
        assertEquals(productDescription, productDTO.getProductDescription());
        assertEquals(productStock, productDTO.getProductStock());
        assertEquals(productPrice, productDTO.getProductPrice(), 0.01);
        assertEquals(productThumbnail, productDTO.getProductThumbnail());
    }

    @Test
    public void testProductBuilderNegativePrice() {
        // Arrange
        double productPrice = -99.99;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductDTOBuilder().withProductPrice(productPrice).build();
        });
    }

    @Test
    public void testProductBuilderZeroPrice() {
        // Arrange
        double productPrice = 0.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductDTOBuilder().withProductPrice(productPrice).build();
        });
    }

    @Test
    public void testProductBuilderNegativeStock() {
        // Arrange
        int productStock = -10;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductDTOBuilder().withProductStock(productStock).build();
        });
    }
}

