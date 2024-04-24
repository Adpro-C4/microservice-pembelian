package com.adpro.pembelian.model.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DTOCustomerDetails {
    private String fullname;
    private String username;
    private String userId;
    private String phoneNumber;
    private String email;
}
