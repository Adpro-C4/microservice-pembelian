package com.adpro.pembelian.service;
import com.adpro.pembelian.model.dto.DTOProduct;
import com.adpro.pembelian.service.external.APIProductServiceImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private APIProductServiceImpl productService;

    @SuppressWarnings("unchecked")
    @Test
    void testGetProductFromAPI() {

        String id = "1";
        JsonNode jsonNode = createSampleJsonNode();
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonNode);

        DTOProduct product = productService.getProductFromAPI(id);

        assertNotNull(product);
        assertEquals("1", product.getProductId());
        assertEquals("Kursi", product.getProductName());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAllProductFromAPI() {
        JsonNode jsonNode = createSampleJsonNode();
        JsonNode jsonArray = new ObjectMapper().createArrayNode().add(jsonNode);

        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(jsonArray);

        List<DTOProduct> products = productService.getAllProductFromAPI();

        assertFalse(products.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetProductFromAPINotFound() {
        String id = "1";

     
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(null);

        assertThrows(
                AssertionError.class,
                () -> productService.getProductFromAPI(id)
        );

      
        verify(restTemplate, times(1)).getForObject(any(String.class), any(Class.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAllProductFromAPINotFound() {
   
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(null);

        assertThrows(
                AssertionError.class,
                () -> productService.getAllProductFromAPI()
        );


        verify(restTemplate, times(1)).getForObject(any(String.class), any(Class.class));
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

