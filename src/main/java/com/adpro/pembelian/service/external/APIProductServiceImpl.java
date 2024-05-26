package com.adpro.pembelian.service.external;
import com.adpro.pembelian.enums.ProductAPI;
import com.adpro.pembelian.model.dto.DTOProduct;
import com.adpro.pembelian.model.builder.ProductBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
public class APIProductServiceImpl implements APIProductService {

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public DTOProduct getProductFromAPI(String id) {
        String url = ProductAPI.PRODUCT.getUrl()+"/"+id;
        JsonNode json = restTemplate.getForObject(url, JsonNode.class);
        assert json != null;
        return  getProduct(json);
    }

    public DTOProduct getProduct(JsonNode json){
        ProductBuilder productBuilder = new ProductBuilder();
        productBuilder.withProductId(json.get("id").textValue())
                .withProductName(json.get("name").textValue())
                .withProductDescription(json.get("description").textValue())
                .withBrand(json.get("brand").textValue())
                .withProductPrice(json.get("price").asDouble())
                .withProductStock(json.get("quantity").asInt())
                .withProductThumbnail(json.get("image").textValue());
        return productBuilder.build();
    }

    @Override
    public List<DTOProduct> getAllProductFromAPI() {
        String url = ProductAPI.ALL_PRODUCT.getUrl();
        List<DTOProduct> products = new ArrayList<>();
        JsonNode json = restTemplate.getForObject(url, JsonNode.class);
        System.out.println(json);
        assert json != null;
        json.forEach(data-> products.add(getProduct(data)));
        return  products;
    }
}
