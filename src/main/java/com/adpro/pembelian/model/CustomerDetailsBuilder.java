package com.adpro.pembelian.model;

public class CustomerDetailsBuilder {
    private String fullname;
    private String username;
    private String userId;
    private String phoneNumber;
    private String email;

    public CustomerDetailsBuilder withFullname(String fullname){
        this.fullname = fullname;
        return  this;
    }
    public  CustomerDetailsBuilder withUsername(String username){
        this.username = username;
        return  this;
    }
    public  CustomerDetailsBuilder withUserId(String userId){
        this.userId = userId;
        return this;
    }

    public  CustomerDetailsBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public CustomerDetailsBuilder withPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerDetails build(){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setEmail(this.email);
        customerDetails.setFullname(this.fullname);
        customerDetails.setUsername(this.username);
        customerDetails.setPhoneNumber(this.phoneNumber);
        customerDetails.setUserId(this.userId);
        return customerDetails;
    }

}
