package com.glowcorner.backend.controller.OrderController;

import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.model.DTO.Order.OrderDTO;
import com.glowcorner.backend.model.DTO.Order.OrderInfoDTO;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Order Management System (Customer)", description = "Operations pertaining to orders in the Order Management System")
@RestController
@RequestMapping("/api/orders/customer/{userID}")
public class CustomerOrderController {

    private final OrderService orderService;

    public CustomerOrderController (OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order *from cart*", description = "Add a new order to the system")
    @PostMapping("/create")
    public ResponseEntity<ResponseData> createOrder(@PathVariable String userID) {
        try {
            OrderDTO createdOrder = orderService.customerCreateOrder(userID);
            createdOrder.setCustomerID(userID);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseData(201, true, "Order created", createdOrder, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Order creation failed: " + e.getMessage(), null, null, null));
        }
    }


    // Get orders by customer ID
    @Operation(summary = "Get orders by customer ID", description = "Retrieve a list of orders using the customer ID")
    @GetMapping
    public ResponseEntity<ResponseData> getOrdersByCustomerID(@PathVariable String userID) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerID(userID);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "No orders found for customer ID: " + userID, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get orders by status
    @Operation(summary = "Get orders by status", description = "Retrieve a list of orders using the status")
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getOrdersByStatus(@PathVariable String userID, @PathVariable String status) {
        List<OrderDTO> orders = orderService.getOrdersByStatusAndCustomerID(status, userID);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "No orders found with status: " + status, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get orders by order date
    @Operation(summary = "Get orders by order date", description = "Retrieve a list of orders using the order date")
    @GetMapping("/date/{orderDate}")
    public ResponseEntity<ResponseData> getOrdersByOrderDate(@PathVariable String userID, @PathVariable LocalDate orderDate) {
        List<OrderDTO> orders = orderService.getOrdersByOrderDateAndCustomerID(orderDate, userID);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "No orders found with order date: " + orderDate, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get order detail by order ID
    @Operation(summary = "Get order info by order ID", description = "Retrieve detailed order information including customer info and order details")
    @GetMapping("/{orderID}/details")
    public ResponseEntity<ResponseData> getOrderDetailByOrderID(@PathVariable String userID, @PathVariable String orderID) {
        OrderInfoDTO orderInfo = orderService.getOrderInfoByOrderID(userID, orderID);
        return ResponseEntity.ok(new ResponseData(200, true, "Order details found", orderInfo, null, null));
    }

    // Update order payment info
    @Operation(
            summary = "Update payment info for an order",
            description = "Attach payment info (e.g., Stripe PaymentIntent ID or mark as COD) to an existing order"
    )
    @PostMapping("/{orderID}/payment")
    public ResponseEntity<ResponseData> updateOrderPaymentInfo(
            @PathVariable String userID,
            @PathVariable String orderID,
            @RequestParam PaymentMethod methodType,
            @RequestBody PaymentInfo paymentInfo
    ) {
        try {
            // Make sure order belongs to user (optional validation)
            OrderInfoDTO order = orderService.getOrderInfoByOrderID(userID, orderID);
            if (order == null) {
                return ResponseEntity.status(404).body(
                        new ResponseData(404, false, "Order not found or does not belong to user", null, null, null)
                );
            }

            // Update payment
            orderService.updatePaymentInfo(orderID, methodType, paymentInfo);

            // Fetch and return updated order info
            OrderDTO updatedOrder = orderService.getOrderById(orderID);
            return ResponseEntity.ok(new ResponseData(200, true, "Payment info updated", updatedOrder, null, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResponseData(400, false, "Invalid payment method: " + e.getMessage(), null, null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ResponseData(500, false, "Payment update failed: " + e.getMessage(), null, null, null)
            );
        }
    }


}
