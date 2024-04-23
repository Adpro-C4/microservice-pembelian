package com.adpro.pembelian.service;

import com.adpro.pembelian.enums.UserApi;
import com.adpro.pembelian.model.CustomerDetails;
import com.adpro.pembelian.model.CustomerDetailsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public CustomerDetails getUserDetailsAPI(String userId) {
        String url = UserApi.GET_USERDATA.getUrl()+"/"+userId;
        try {
            JsonNode node = restTemplate.getForEntity(url, JsonNode.class).getBody();
            System.out.println(node);
            assert node != null;
            return getUserDetails(node.get("data").get("user_detail"));

        }catch (Exception e){
            System.out.println(e);
            return  null;
        }
    }

    private  CustomerDetails getUserDetails(JsonNode node){
      return new CustomerDetailsBuilder()
              .withEmail(node.get("email").textValue())
              .withFullname(node.get("name").textValue())
              .withUserId(node.get("id").textValue())
              .withPhoneNumber(node.get("phoneNumber").textValue())
              .withUsername(node.get("username").textValue())
              .build();
    };
}
