package com.adpro.pembelian.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerDetailsTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String fullname = "John Doe";
        String username = "johndoe";
        String userId = "123456";
        String phoneNumber = "1234567890";
        String email = "johndoe@example.com";

        // Act
        CustomerDetails customer = new CustomerDetails();
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
    public void testSetters() {
        // Arrange
        CustomerDetails customer = new CustomerDetails();

        // Act
        customer.setFullname("Jane Doe");
        customer.setUsername("janedoe");
        customer.setUserId("654321");
        customer.setPhoneNumber("9876543210");
        customer.setEmail("janedoe@example.com");

        // Assert
        assertEquals("Jane Doe", customer.getFullname());
        assertEquals("janedoe", customer.getUsername());
        assertEquals("654321", customer.getUserId());
        assertEquals("9876543210", customer.getPhoneNumber());
        assertEquals("janedoe@example.com", customer.getEmail());
    }

    // Add more test cases as needed
}
