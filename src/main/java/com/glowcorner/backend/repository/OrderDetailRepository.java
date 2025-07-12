package com.glowcorner.backend.repository;

import com.glowcorner.backend.entity.mongoDB.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends MongoRepository<OrderDetail, String> {
    Optional<OrderDetail> findByOrderIDAndProductID(String orderId, String productID);

    List<OrderDetail> findAllByOrderID(String orderID);


}
