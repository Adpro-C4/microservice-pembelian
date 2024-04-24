package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.service.internal.PricingStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Order {
    protected String userId;
    protected   String timestamp;
    @Embedded
    protected DTOCustomerDetails customerDetails;
    protected String address;
    @ManyToMany(mappedBy = "order", cascade = CascadeType.ALL)
    protected List<CartItem> cartItems;
    @Transient
    protected PricingStrategy<CartItem> strategy;
    protected String price;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract double getTotalPrice();
}
