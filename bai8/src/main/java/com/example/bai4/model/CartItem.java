package com.example.bai4.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * CartItem represents a product in the shopping cart with its quantity
 * Implements Serializable to allow storing in session
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer productId;
    private String productName;
    private Long price;
    private Integer quantity;
    private String image;
    
    // Calculate total price for this item
    public Long getTotalPrice() {
        return price * quantity;
    }
}
