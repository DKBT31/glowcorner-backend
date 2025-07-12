package com.glowcorner.backend.controller.OrderController;

import com.glowcorner.backend.enums.Status.OrderStatus;
import com.glowcorner.backend.model.DTO.Order.OrderDTO;
import com.glowcorner.backend.model.DTO.Order.OrderDetailDTO;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Order Management System", description = "Operations pertaining to orders in the Order Management System")
@RestController
@RequestMapping("/api/orders/staff")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* Order CRUD
    * */

    // Update order
    @Operation(summary = "Update an existing order", description = "Update an existing order using its ID")
    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseData> updateOrder(
            @PathVariable String orderId,
            @RequestBody Map<String, String> body) {
        try {
            String statusStr = body.get("status");
            OrderStatus status = OrderStatus.valueOf(statusStr);
            OrderDTO updatedOrder = orderService.updateOrder(orderId, status);
            if (updatedOrder == null) {
                return ResponseEntity.status(404)
                        .body(new ResponseData(404, false, "Order with ID: " + orderId + " not found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Order updated", updatedOrder, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(400, false, e.getMessage(), null, null, null));
        }
    }

    // Delete order
    @Operation(summary = "Delete an order by ID", description = "Remove an order from the system using its ID")
    @DeleteMapping("/id/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    /* Order Query
    * */

    // Get order by id
    @Operation(summary = "Get an order by ID", description = "Retrieve a single order using its ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseData> getOrderById(@PathVariable String orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        if (orderDTO == null) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "Order with ID: " + orderId + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Order found", orderDTO, null, null));
    }

    // Get all orders
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get orders by customer id
    @Operation(summary = "Get orders by customer ID", description = "Retrieve a list of orders using the customer ID")
    @GetMapping("/customer/{customerID}")
    public ResponseEntity<ResponseData> getOrdersByCustomerID(@PathVariable String customerID) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerID(customerID);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "There is no order with customer ID: " + customerID, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get orders by customer name
    @Operation(summary = "Get orders by customer name", description = "Retrieve a list of orders using the customer name")
    @GetMapping("/customer/name/{customerName}")
    public ResponseEntity<ResponseData> getOrdersByCustomerName(@PathVariable String customerName) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerName(customerName);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "There is no order with customer name: " + customerName, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get orders by status
    @Operation(summary = "Get orders by status", description = "Retrieve a list of orders using the status")
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getOrdersByStatus(@PathVariable String status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        if (orders.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }

    // Get orders by order date
    @Operation(summary = "Get orders by order date", description = "Retrieve a list of orders using the order date")
    @GetMapping("/date/{orderDate}")
    public ResponseEntity<ResponseData> getOrdersByOrderDate(@PathVariable LocalDate orderDate) {
        List<OrderDTO> orders = orderService.getOrdersByOrderDate(orderDate);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new ResponseData(404, false, "There is no order with order date: " + orderDate, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Orders found", orders, null, null));
    }



    /* OrderDetail CRUD
    * */

    // Create order detail
    @Operation(summary = "Create a new order detail", description = "Add a new order detail to the system")
    @PostMapping("/{orderId}/details")
    public ResponseEntity<ResponseData> createOrderDetail(@PathVariable String orderId, @RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailDTO createdOrderDetail = orderService.createOrderDetail(orderId, orderDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(201, true, "Order detail created", createdOrderDetail, null, null));
    }

    // Update order detail
    @Operation(summary = "Update an existing order detail", description = "Update an existing order detail using its ID")
    @PutMapping("/{orderId}/details/{productID}")
    public ResponseEntity<ResponseData> updateOrderDetail(@PathVariable String orderId, @PathVariable String productID, @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetailDTO updatedOrderDetail = orderService.updateOrderDetail(orderId, productID, orderDetailDTO);
            if (updatedOrderDetail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseData(404, false, "Order detail with ID: " + productID + " not found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Order detail updated", updatedOrderDetail, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(400, false, e.getMessage(), null, null, null));
        }
    }

    // Delete order detail
    @Operation(summary = "Delete an order detail by ID", description = "Remove an order detail from the system using its ID")
    @DeleteMapping("/{orderId}/details/{productID}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable String orderId, @PathVariable String productID) {
        orderService.deleteOrderDetail(orderId, productID);
        return ResponseEntity.noContent().build();
    }

    /* OrderDetail Query
    * */

    // Get all order details
    @Operation(summary = "Get all order details", description = "Retrieve a list of all order details")
    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails(@PathVariable String orderId) {
        List<OrderDetailDTO> orderDetails = orderService.getOrderDetailByOrderID(orderId);
        return ResponseEntity.ok(orderDetails);
    }
}