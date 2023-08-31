package com.programmingtechie.order.service.repository;

import com.programmingtechie.order.service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
