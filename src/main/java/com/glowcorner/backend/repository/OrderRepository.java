package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByOrderID(String orderId);
    List<Order> findByCustomerID(String customerID);
    List<Order> findByCustomerName(String customerName);
    List<Order> findByStatus(String status);
    List<Order> findByOrderDate(LocalDate orderDate);

    List<Order> findByStatusAndCustomerID(String status, String userID);

    List<Order> findByOrderDateAndCustomerID(LocalDate orderDate, String userID);
    Optional<Order> findByOrderIDAndCustomerID(String orderID, String customerID);
}

