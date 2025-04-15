package com.example.ecomm_orderservice.repository;

import com.example.ecomm_orderservice.entity.Order;
import com.example.ecomm_orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
    List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);
    List<Order> findByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> orderStatus);

    Optional<Order> findByTrackingId(UUID trackingId);

    List<Order> findByDateBetweenAndOrderStatus(Date startDate, Date endDate, OrderStatus status);

    Long countByOrderStatus(OrderStatus status);
    //Optional<Order> findByTrackingId(UUID trackingId);
}
