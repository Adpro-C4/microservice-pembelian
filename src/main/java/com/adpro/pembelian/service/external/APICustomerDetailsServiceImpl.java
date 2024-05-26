package com.adpro.pembelian.service.external;

import com.adpro.pembelian.enums.UserAPI;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.model.builder.CustomerDetailsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APICustomerDetailsServiceImpl implements APICustomerDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public DTOCustomerDetails getUserDetailsAPI(String userId) {
        String url = UserAPI.GET_USERDATA.getUrl()+"/"+userId;
        try {
            JsonNode node = restTemplate.getForEntity(url, JsonNode.class).getBody();
            return getUserDetails(node.get("data").get("user_detail"));

        }catch (Exception e){
            return  null;
        }
    }

    private DTOCustomerDetails getUserDetails(JsonNode node){
      return new CustomerDetailsBuilder()
              .withEmail(node.get("email").textValue())
              .withFullname(node.get("name").textValue())
              .withUserId(node.get("id").textValue())
              .withPhoneNumber(node.get("phoneNumber").textValue())
              .withUsername(node.get("username").textValue())
              .build();
    };
}
