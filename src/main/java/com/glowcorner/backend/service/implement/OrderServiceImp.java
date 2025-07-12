package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.*;
import com.glowcorner.backend.enums.PaymentMethod;
import com.glowcorner.backend.enums.Status.OrderStatus;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.model.DTO.Order.OrderDTO;
import com.glowcorner.backend.model.DTO.Order.OrderDetailDTO;
import com.glowcorner.backend.model.DTO.Order.OrderInfoDTO;
import com.glowcorner.backend.model.DTO.Order.PaymentInfo;
import com.glowcorner.backend.model.mapper.Order.OrderDetailMapper;
import com.glowcorner.backend.model.mapper.Order.OrderMapper;
import com.glowcorner.backend.repository.*;
import com.glowcorner.backend.service.interfaces.OrderService;
import com.glowcorner.backend.service.interfaces.payment.PaymentProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    private final OrderDetailMapper orderDetailMapper;

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessors;

    private final CartRepository cartRepository;

    private final PromotionRepository promotionRepository;
    private final CounterServiceImpl counterServiceImpl;

    public OrderServiceImp(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper, OrderDetailMapper orderDetailMapper, List<PaymentProcessor> processors, CartRepository cartRepository, PromotionRepository promotionRepository, CounterServiceImpl counterServiceImpl) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.paymentProcessors = processors.stream()
                .collect(Collectors.toMap(PaymentProcessor::getSupportedMethod, Function.identity()));
        this.cartRepository = cartRepository;
        this.promotionRepository = promotionRepository;
        this.counterServiceImpl = counterServiceImpl;
    }

    /* Order
    * */

    @Override
    public OrderDTO customerCreateOrder(String userID) {
        User user = userRepository.findByUserID(userID)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (user.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Customer ID required!");
        }

        Cart cart = cartRepository.findByUserID(userID)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Order order = new Order();
        order.setOrderID(counterServiceImpl.getNextOrderID());
        order.setCustomerID(userID);
        order.setCustomerName(user.getFullName());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);

        final String orderID = order.getOrderID();

        List<OrderDetail> orderDetails = cart.getItems().stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderID(orderID);
            orderDetail.setProductID(cartItem.getProductID());
            orderDetail.setProductName(cartItem.getProductName());
            orderDetail.setQuantity(cartItem.getQuantity());

            Product product = productRepository.findByProductID(cartItem.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderDetail.setProductPrice(product.getPrice());

            orderDetail.setTotalAmount(cartItem.getTotalAmount());
            orderDetail.setDiscountedTotalAmount(cartItem.getDiscountedTotalAmount());

            Promotion promotion = promotionRepository.findActivePromotion(LocalDate.now(), LocalDate.now(), List.of(cartItem.getProductID()))
                    .orElse(null);
            if (promotion != null) {
                orderDetail.setDiscountName(promotion.getPromotionName());
                orderDetail.setDiscountPercentage(promotion.getDiscount());
            } else {
                orderDetail.setDiscountName(null);
                orderDetail.setDiscountPercentage(null);
            }

            return orderDetail;
        }).collect(Collectors.toList());

        // Set order details and calculate total amount
        order.setOrderDetails(orderDetails);
        order.setTotalAmount(calculateTotalAmount(orderDetails));
        order.setDiscountedTotalAmount(calculateDiscountedTotalAmount(orderDetails));

        order = orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);

        // Clear cart
        cart.getItems().clear();
        cart.setTotalAmount(0L);
        cart.setDiscountedTotalAmount(null);
        cartRepository.save(cart);

        return orderMapper.toOrderDTO(order);
    }


    // Update order
    @Override
    public OrderDTO updateOrder(String orderId, OrderStatus status) {
        // Find existing order
        Order existingOrder = orderRepository.findByOrderID(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update
        existingOrder.setStatus(status);

        // If the order is completed, add loyalty points to the user
        if (status == OrderStatus.COMPLETED) {
            User user = userRepository.findByUserID(existingOrder.getCustomerID())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Calculate loyalty points (1 point for every $1 paid)
            long loyaltyPoints = existingOrder.getTotalAmount();
            user.setLoyalPoints((int) (user.getLoyalPoints() + loyaltyPoints));

            // Save the updated user
            userRepository.save(user);
        }

        // Save
        Order savedOrder = orderRepository.save(existingOrder);

        // Convert to DTO
        return orderMapper.toOrderDTO(savedOrder);
    }

    // Delete order
    @Override
    public void deleteOrder(String orderId) {
        Order existingOrder = orderRepository.findByOrderID(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setStatus(OrderStatus.DISABLE);
        orderRepository.save(existingOrder);
    }



    // Get all orders
    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Get order by order ID
    @Override
    public OrderDTO getOrderById(String orderId) {
        if (orderRepository.findByOrderID(orderId).isPresent())
            return orderMapper.toOrderDTO(orderRepository.findByOrderID(orderId).get());
        return null;
    }

    // Get orders by customer ID
    @Override
    public List<OrderDTO> getOrdersByCustomerID(String customerID) {
        List<Order> orders = orderRepository.findByCustomerID(customerID);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Get orders by customer name
    @Override
    public List<OrderDTO> getOrdersByCustomerName(String customerName) {
        List<Order> orders = orderRepository.findByCustomerName(customerName);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Get orders by status
    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Get orders by order date
    @Override
    public List<OrderDTO> getOrdersByOrderDate(LocalDate orderDate) {
        List<Order> orders = orderRepository.findByOrderDate(orderDate);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }


    // \\ For customer \\
    // Get orders by status and customerID
    public List<OrderDTO> getOrdersByStatusAndCustomerID(String status, String userID) {
        List<Order> orders = orderRepository.findByStatusAndCustomerID(status, userID);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // Get orders by order date and customerID
    public List<OrderDTO> getOrdersByOrderDateAndCustomerID(LocalDate orderDate, String userID) {
        List<Order> orders = orderRepository.findByOrderDateAndCustomerID(orderDate, userID);
        return orders.stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }



    /* OrderDetail
    * */

    // Create order detail
    @Override
    public OrderDetailDTO createOrderDetail(String orderId, OrderDetailDTO orderDetailDTO) {
        orderDetailDTO.setOrderID(orderId);
        OrderDetail orderDetail = orderDetailMapper.toOrderDetail(orderDetailDTO, orderId);
        orderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toOrderDetailDTO(orderDetail);
    }

    // Update order detail
    @Override
    public OrderDetailDTO updateOrderDetail(String orderID, String productID, OrderDetailDTO orderDetailDTO) {
        try{
        OrderDetail existingOrderDetail = orderDetailRepository.findByOrderIDAndProductID(orderID, productID)
                .orElseThrow(() -> new RuntimeException("Order detail not found"));

            if (orderDetailDTO.getOrderID() != null) existingOrderDetail.setOrderID(orderDetailDTO.getOrderID());
            if (orderDetailDTO.getProductID() != null) existingOrderDetail.setProductID(orderDetailDTO.getProductID());
            if (orderDetailDTO.getQuantity() != null) existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
            if (orderDetailDTO.getTotalAmount() != null) existingOrderDetail.setTotalAmount(orderDetailDTO.getTotalAmount());

            existingOrderDetail = orderDetailRepository.save(existingOrderDetail);
            return orderDetailMapper.toOrderDetailDTO(existingOrderDetail);
        } catch (Exception e) {
            throw new RuntimeException("Fail to update order detail: " + e.getMessage(), e);
        }
    }

    // Get order info by order ID
    @Override
    public OrderInfoDTO getOrderInfoByOrderID(String userID, String orderID) {
        // 1. Find the Order by orderID and userID
        Order order = orderRepository.findByOrderIDAndCustomerID(orderID, userID)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Find all OrderDetails by orderID
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderID(orderID);
        if (orderDetails.isEmpty()) {
            throw new RuntimeException("Order details not found");
        }

        // 3. Fetch User info by userID
        User user = userRepository.findByUserID(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Fetch Product info for each OrderDetail
        List<OrderInfoDTO.OrderDetailItemDTO> orderDetailItems = orderDetails.stream().map(orderDetail -> {
            // Fetch Product by productID
            Product product = productRepository.findByProductID(orderDetail.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderInfoDTO.OrderDetailItemDTO item = new OrderInfoDTO.OrderDetailItemDTO();
            item.setProductID(orderDetail.getProductID());
            item.setQuantity(orderDetail.getQuantity());
            item.setProductPrice(orderDetail.getProductPrice());
            item.setName(product.getProductName());
            Promotion promotion = promotionRepository.findActivePromotion(LocalDate.now(), LocalDate.now(), List.of(orderDetail.getProductID()))
                    .orElse(null);
            if (promotion != null) {
                item.setDiscount(promotion.getDiscount());
            } else {
                item.setDiscount(null);
            }
            item.setImage(product.getImage_url());
            return item;
        }).collect(Collectors.toList());

        // 5. Calculate total amount
        Long totalAmount = calculateTotalAmount(orderDetails);

        // 6. Build the response
        OrderInfoDTO response = new OrderInfoDTO();
        response.setOrderID(orderID);
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());

        OrderInfoDTO.CustomerInfoDTO customerInfo = new OrderInfoDTO.CustomerInfoDTO();
        customerInfo.setName(user.getFullName());
        customerInfo.setEmail(user.getEmail());
        customerInfo.setPhone(user.getPhone());
        customerInfo.setAddress(user.getAddress());
        response.setCustomerInfo(customerInfo);

        response.setOrderDetails(orderDetailItems);
        response.setTotalAmount(totalAmount);

        return response;
    }

    // Update payment info
    public void updatePaymentInfo(String orderId, PaymentMethod methodType, PaymentInfo paymentInfo) {
        // Retrieve the order first
        Order order = orderRepository.findByOrderID(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Find the appropriate processor based on the method type
        PaymentProcessor processor = paymentProcessors.get(methodType);
        if (processor == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + methodType);
        }

        // Process payment using the correct processor
        processor.processPayment(order, paymentInfo);

        // Save the updated order after payment info is added
        orderRepository.save(order);
    }

    // Delete order detail
    @Override
    public void deleteOrderDetail(String orderID, String productID) {
        OrderDetail existingOrderDetail = orderDetailRepository.findByOrderIDAndProductID(orderID, productID)
                .orElseThrow(() -> new RuntimeException("Order detail not found"));

        orderDetailRepository.delete(existingOrderDetail);
    }

    // Get order details by order ID
    @Override
    public List<OrderDetailDTO> getOrderDetailByOrderID(String orderID) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderID(orderID);

        return orderDetails.stream()
                .map(orderDetailMapper::toOrderDetailDTO)
                .collect(Collectors.toList());
    }



    /* Calculate Total Amount */
    private Long calculateTotalAmount(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .mapToLong(OrderDetail::getTotalAmount)
                .sum();
    }

    /* Calculate Discounted Total Amount */
    private Long calculateDiscountedTotalAmount(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .mapToLong(orderDetail -> {
                    if (orderDetail.getDiscountedTotalAmount() != null) {
                        return orderDetail.getDiscountedTotalAmount();
                    } else {
                        return orderDetail.getTotalAmount();
                    }
                })
                .sum();
    }
}
