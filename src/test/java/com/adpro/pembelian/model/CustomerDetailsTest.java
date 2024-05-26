package com.adpro.pembelian.model;
import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerDetailsTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String fullname = "John Doe";
        String username = "johndoe";
        String userId = "123456";
        String phoneNumber = "1234567890";
        String email = "johndoe@example.com";

        // Act
        DTOCustomerDetails customer = new DTOCustomerDetails();
        customer.setFullname(fullname);
        customer.setUsername(username);
        customer.setUserId(userId);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);

        // Assert
        assertEquals(fullname, customer.getFullname());
        assertEquals(username, customer.getUsername());
        assertEquals(userId, customer.getUserId());
        assertEquals(phoneNumber, customer.getPhoneNumber());
        assertEquals(email, customer.getEmail());
    }

    @Test
    void testSetters() {
        // Arrange
        DTOCustomerDetails customer = new DTOCustomerDetails();

        // Act
        customer.setFullname("Jane Doe");
        customer.setUsername("janedoe");
        customer.setUserId("654321");
        customer.setPhoneNumber("9876543210");
        customer.setEmail("janedoe@example.com");

        assertEquals("Jane Doe", customer.getFullname());
        assertEquals("janedoe", customer.getUsername());
        assertEquals("654321", customer.getUserId());
        assertEquals("9876543210", customer.getPhoneNumber());
        assertEquals("janedoe@example.com", customer.getEmail());
    }

  
}
