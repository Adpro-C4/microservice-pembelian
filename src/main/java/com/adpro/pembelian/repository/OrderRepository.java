package com.adpro.pembelian.repository;

import com.adpro.pembelian.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
