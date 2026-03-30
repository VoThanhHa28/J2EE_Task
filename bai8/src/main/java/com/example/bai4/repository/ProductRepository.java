package com.example.bai4.repository;

import com.example.bai4.model.Category;
import com.example.bai4.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Search by name (keyword)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);
    
    // Filter by category
    Page<Product> findByCategory(Category category, Pageable pageable);
    
    // Search by name with pagination
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Get all products with pagination
    Page<Product> findAll(Pageable pageable);
}
