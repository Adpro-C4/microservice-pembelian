package com.adpro.pembelian.service;


import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.service.external.APICustomerDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CustomerDetailsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private APICustomerDetailsServiceImpl customerDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetUserDetailsAPI() {
        String userId = "123";
        String name = "John Doe";
        String email = "john@example.com";
        String phoneNumber = "1234567890";
        String username = "johndoe";

        JsonNode userDataNode = createSampleJsonNode(name, email, phoneNumber, username);
        JsonNode responseDataNode = createSampleResponseJsonNode(userDataNode);

        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenReturn(new ResponseEntity<>(responseDataNode, HttpStatus.OK));

        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(userId);

        assertEquals(name, customerDetails.getFullname());
        assertEquals(email, customerDetails.getEmail());
        assertEquals(phoneNumber, customerDetails.getPhoneNumber());
        assertEquals(username, customerDetails.getUsername());
        assertEquals(userId, customerDetails.getUserId());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetUserDetailsAPIUserNotFound() {
        String userId = "123";
        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(userId);

        assertNull(customerDetails);
        verify(restTemplate, times(1)).getForEntity(anyString(), any(Class.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetUserDetailsAPIException() {
        String userId = "123";
        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenThrow(new RuntimeException("API error"));

        DTOCustomerDetails customerDetails = customerDetailsService.getUserDetailsAPI(userId);

        assertNull(customerDetails);
        verify(restTemplate, times(1)).getForEntity(anyString(), any(Class.class));
    }

    private JsonNode createSampleJsonNode(String name, String email, String phoneNumber, String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", name);
        node.put("email", email);
        node.put("phoneNumber", phoneNumber);
        node.put("username", username);
        node.put("id", "123");
        return node;
    }

    private JsonNode createSampleResponseJsonNode(JsonNode userDataNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseDataNode = objectMapper.createObjectNode();
        responseDataNode.set("data", objectMapper.createObjectNode().set("user_detail", userDataNode));
        return responseDataNode;
    }
}
