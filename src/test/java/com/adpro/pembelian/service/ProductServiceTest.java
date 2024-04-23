package com.adpro.pembelian.service;
import com.adpro.pembelian.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testGetProductFromAPI() {

        String id = "1";
        JsonNode jsonNode = createSampleJsonNode();
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonNode);

        Product product = productService.getProductFromAPI(id);

        assertNotNull(product);
        assertEquals("1", product.getProductId());
        assertEquals("Kursi", product.getProductName());
    }

    @Test
    public void testGetAllProductFromAPI() {
        JsonNode jsonNode = createSampleJsonNode();
        JsonNode jsonArray = new ObjectMapper().createArrayNode().add(jsonNode);

        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonArray);

        List<Product> products = productService.getAllProductFromAPI();

        assertFalse(products.isEmpty());
    }

    private JsonNode createSampleJsonNode() {
        String sampleJson = "{\"id\":\"1\",\"name\":\"Kursi\",\"description\":\"\",\"price\":100,\"discount\":0,\"brand\":\"Brand1\",\"category\":\"Category1\",\"image\":\"image1.jpg\",\"quantity\":10}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(sampleJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

