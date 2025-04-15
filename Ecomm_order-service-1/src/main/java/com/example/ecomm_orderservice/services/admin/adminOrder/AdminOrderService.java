package com.example.ecomm_orderservice.services.admin.adminOrder;

import com.example.ecomm_orderservice.dto.AnalyticsResponse;
import com.example.ecomm_orderservice.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlacedOrders();
    OrderDto changeOrderStatus (Long orderId, String status);
    AnalyticsResponse calculateAnalytics();
}
