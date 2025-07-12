package com.glowcorner.backend.model.mapper.Order;

import com.glowcorner.backend.entity.mongoDB.OrderDetail;
import com.glowcorner.backend.model.DTO.Order.OrderDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    // Convert OrderDetail entity to OrderDetailDTO
    public OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        return new OrderDetailDTO(
            orderDetail.getOrderID(),
            orderDetail.getProductID(),
            orderDetail.getProductName(),
            orderDetail.getQuantity(),
            orderDetail.getProductPrice(),
            orderDetail.getDiscountName(),
            orderDetail.getDiscountPercentage(),
            orderDetail.getTotalAmount(),
            orderDetail.getDiscountedTotalAmount()
        );
    }

    // Convert OrderDetailDTO to OrderDetail entity
    public OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO, String orderID) {
        if (orderDetailDTO == null) {
            return null;
        }

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderID(orderID);
        orderDetail.setProductID(orderDetailDTO.getProductID());
        orderDetail.setProductName(orderDetailDTO.getProductName()); // Map this field
        orderDetail.setQuantity(orderDetailDTO.getQuantity());
        orderDetail.setProductPrice(orderDetailDTO.getProductPrice());
        orderDetail.setDiscountName(orderDetailDTO.getDiscountName());
        orderDetail.setDiscountPercentage(orderDetailDTO.getDiscountPercentage());
        orderDetail.setTotalAmount(orderDetailDTO.getTotalAmount());
        orderDetail.setDiscountedTotalAmount(orderDetailDTO.getDiscountedTotalAmount());

        return orderDetail;
    }
}
