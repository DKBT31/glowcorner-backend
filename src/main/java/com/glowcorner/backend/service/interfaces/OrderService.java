package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.enums.Status.OrderStatus;
import com.glowcorner.backend.model.DTO.Order.OrderDTO;
import com.glowcorner.backend.model.DTO.Order.OrderDetailDTO;
import com.glowcorner.backend.model.DTO.Order.OrderInfoDTO;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    /* Order CRUD */
    OrderDTO customerCreateOrder(String userID);
    OrderDTO updateOrder(String orderId, OrderStatus status);
    void deleteOrder(String orderId);

    /* Order Query */
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(String orderId);
    List<OrderDTO> getOrdersByCustomerID(String customerID);
    List<OrderDTO> getOrdersByCustomerName(String customerName);
    List<OrderDTO> getOrdersByStatus(String status);
    List<OrderDTO> getOrdersByStatusAndCustomerID(String status, String userID);
    List<OrderDTO> getOrdersByOrderDate(LocalDate orderDate);
    List<OrderDTO> getOrdersByOrderDateAndCustomerID(LocalDate orderDate, String userID);

    /* OrderDetail CRUD */
    OrderDetailDTO createOrderDetail(String orderID, OrderDetailDTO orderDetailDTO);
    OrderDetailDTO updateOrderDetail(String orderID, String productID, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(String orderID, String productID);

    /* OrderDetail Query */
    List<OrderDetailDTO> getOrderDetailByOrderID(String orderID);

    OrderInfoDTO getOrderInfoByOrderID(String userID, String orderID);

    void updatePaymentInfo(String orderId, PaymentMethod methodType, PaymentInfo paymentInfo);

}
