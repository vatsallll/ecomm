package com.example.ecomm_orderservice.services.admin.adminOrder;

import com.example.ecomm_orderservice.dto.AnalyticsResponse;
import com.example.ecomm_orderservice.dto.OrderDto;
import com.example.ecomm_orderservice.entity.NotificationPojo;
import com.example.ecomm_orderservice.entity.Order;
import com.example.ecomm_orderservice.entity.User;
import com.example.ecomm_orderservice.enums.OrderStatus;
import com.example.ecomm_orderservice.repository.OrderRepository;
import com.example.ecomm_orderservice.repository.UserRepository;
import com.example.ecomm_orderservice.services.kafka.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;


    @Autowired
    private final KafkaService kafkaService;

    // Constructor injection


    public List<OrderDto> getAllPlacedOrders() {
        List<Order> orderList = orderRepository.
                findAllByOrderStatusIn (List.of (OrderStatus. Placed, OrderStatus. Shipped, OrderStatus. Delivered));

        return orderList.stream().map(Order::getOrderDto).collect (Collectors.toList());
    }

    public OrderDto changeOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order orders = optionalOrder.orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
        User user = orders.getUser();
        UUID trackingId = orders.getTrackingId();
        String email = user.getEmail();
        String name = user.getName();

        // Prepare NotificationPojo
        NotificationPojo notificationPojo = new NotificationPojo();
        notificationPojo.setName(name);
        notificationPojo.setEmail(email);
        notificationPojo.setTrackingId(trackingId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (Objects.equals(status, "Shipped")) {
                order.setOrderStatus(OrderStatus.Shipped);
                notificationPojo.setStatus("Shipped");
            } else if (Objects.equals(status, "Delivered")) {
                order.setOrderStatus(OrderStatus.Delivered);
                notificationPojo.setStatus("Delivered");
            } else {
                notificationPojo.setStatus("Placed");
            }

            // Convert NotificationPojo to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String notificationJson = objectMapper.writeValueAsString(notificationPojo);
                kafkaService.sendOrderNotification(notificationJson);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to convert notification to JSON", e);
            }

            return orderRepository.save(order).getOrderDto();
        }
        return null;
    }

    public AnalyticsResponse calculateAnalytics () {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthEarnings = getTotalEarningsForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long placed = orderRepository.countByOrderStatus(OrderStatus.Placed);
        Long shipped = orderRepository.countByOrderStatus(OrderStatus.Shipped);
        Long delivered = orderRepository.countByOrderStatus(OrderStatus.Delivered);

        return new AnalyticsResponse(placed, shipped, delivered, currentMonthOrders, previousMonthOrders, currentMonthEarnings, previousMonthEarnings);

    }


    public Long getTotalOrdersForMonth (int month, int year){

        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar. YEAR, year);
        calendar.set(Calendar. MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar. HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar. SECOND, 8);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum (Calendar.DAY_OF_MONTH));
        calendar.set(Calendar. HOUR_OF_DAY, 23);
        calendar.set(Calendar. MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth ,OrderStatus.Delivered);

        return (long) orders.size();
    }

    public Long getTotalEarningsForMonth (int month, int year){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar. YEAR, year);
        calendar.set(Calendar. MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar. HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar. SECOND, 8);

        Date startOfMonth = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum (Calendar.DAY_OF_MONTH));
        calendar.set(Calendar. HOUR_OF_DAY, 23);
        calendar.set(Calendar. MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth, endOfMonth ,OrderStatus.Delivered);

        Long sum = 0L;
        for (Order order : orders) {
            sum += order.getAmount();
        }
        return sum;
    }


}
