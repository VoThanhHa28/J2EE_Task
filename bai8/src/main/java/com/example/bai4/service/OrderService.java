package com.example.bai4.service;

import com.example.bai4.model.CartItem;
import com.example.bai4.model.Order;
import com.example.bai4.model.OrderDetail;
import com.example.bai4.repository.OrderDetailRepository;
import com.example.bai4.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    /**
     * Create order from cart items
     */
    @Transactional
    public Order createOrder(Order order, List<CartItem> cartItems) {
        // Calculate and set total price
        long totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        order.setTotalPrice(totalPrice);
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Create order details
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProductId(item.getProductId());
            detail.setProductName(item.getProductName());
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        
        return savedOrder;
    }
    
    /**
     * Get order by id
     */
    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
