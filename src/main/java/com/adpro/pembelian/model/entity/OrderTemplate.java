package com.adpro.pembelian.model.entity;

import com.adpro.pembelian.model.dto.DTOCustomerDetails;
import com.adpro.pembelian.service.internal.PricingStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ORDERS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OrderTemplate implements Serializable {
    protected String userId;
    protected   String timestamp;
    protected String shippingMethod;
    protected String resi;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "customer_userId"))
    })
    protected DTOCustomerDetails customerDetails;
    protected String address;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    protected List<CartItemEntity> cartItems;
    @Transient
    @JsonIgnore
    protected PricingStrategy<CartItemEntity> strategy;
    protected String price;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    public abstract double getTotalPrice();
}
