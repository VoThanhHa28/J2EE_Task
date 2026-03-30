package com.example.bai4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false)
    private Integer productId;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Long price;
    
    @Column(nullable = false)
    private Integer quantity;
    
    // Calculate total price for this item
    public Long getTotalPrice() {
        return price * quantity;
    }
}
