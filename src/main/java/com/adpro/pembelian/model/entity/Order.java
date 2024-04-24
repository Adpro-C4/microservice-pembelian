package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.service.internal.PricingStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
public abstract class Order implements Serializable {
    @Column(insertable = false, updatable = false)
    protected String userId;
    protected   String timestamp;
    @Embedded
    protected DTOCustomerDetails customerDetails;
    protected String address;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    protected List<CartItem> cartItems;
    @Transient
    protected PricingStrategy<CartItem> strategy;
    protected String price;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract double getTotalPrice();
}
