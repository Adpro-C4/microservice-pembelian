package com.adpro.pembelian.repository;

import com.adpro.pembelian.model.entity.OrderTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderTemplate, Long> {
    List<OrderTemplate> findByUserId(String userId);
    List<OrderTemplate> deleteByUserId(String userId);
}
