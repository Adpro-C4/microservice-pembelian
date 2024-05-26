package com.adpro.pembelian.model;
import com.adpro.pembelian.model.dto.DTOProduct;
import com.adpro.pembelian.model.builder.ProductBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductBuilderTest {

    @Test
    void testProductBuilder() {
        // Arrange
        String productId = "123";
        String productName = "Test Product";
        String productDescription = "This is a test product";
        int productStock = 10;
        double productPrice = 99.99;
        String productThumbnail = "test_thumbnail.jpg";

        // Act
        DTOProduct product = new ProductBuilder()
                .withProductId(productId)
                .withProductName(productName)
                .withProductDescription(productDescription)
                .withProductStock(productStock)
                .withProductPrice(productPrice)
                .withProductThumbnail(productThumbnail)
                .build();

        // Assert
        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
        assertEquals(productDescription, product.getProductDescription());
        assertEquals(productStock, product.getProductStock());
        assertEquals(productPrice, product.getProductPrice(), 0.01);
        assertEquals(productThumbnail, product.getProductThumbnail());
    }

    @Test
    void testProductBuilderNegativePrice() {
        // Arrange
        double productPrice = -99.99;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductBuilder().withProductPrice(productPrice).build();
        });
    }



    @Test
    void testProductBuilderNegativeStock() {
        // Arrange
        int productStock = -10;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductBuilder().withProductStock(productStock).build();
        });
    }
}

