package com.example.bai4.service;

import com.example.bai4.model.Product;
import com.example.bai4.model.Category;
import com.example.bai4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    private static final int PAGE_SIZE = 5;

    // Lấy tất cả sản phẩm từ DB
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo id
    public Product get(int id) {
        Optional<Product> optional = productRepository.findById(id);
        return optional.orElse(null);
    }

    // Thêm hoặc cập nhật sản phẩm
    public void save(Product product) {
        productRepository.save(product);
    }

    // Xoá sản phẩm
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    // Upload & cập nhật ảnh
    public void updateImage(Product product, MultipartFile imageProduct) {
        if (imageProduct != null && !imageProduct.isEmpty()) {
            try {
                Path dirImages = Paths.get("src/main/resources/static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }

                String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);

                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);

                product.setImage(newFileName);

                // Lưu lại vào DB
                productRepository.save(product);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // === NEW METHODS FOR SEARCH, FILTER, SORT, PAGINATION ===
    
    /**
     * Search products by keyword with pagination
     */
    public Page<Product> searchByKeyword(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").ascending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    
    /**
     * Search with sorting by price
     */
    public Page<Product> searchWithSort(String keyword, String sortType, int page) {
        Sort sort;
        
        if ("price-asc".equals(sortType)) {
            sort = Sort.by("price").ascending();
        } else if ("price-desc".equals(sortType)) {
            sort = Sort.by("price").descending();
        } else {
            sort = Sort.by("id").ascending();
        }
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    
    /**
     * Filter products by category with pagination
     */
    public Page<Product> filterByCategory(Category category, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("id").ascending());
        return productRepository.findByCategory(category, pageable);
    }
    
    /**
     * Filter by category and sort
     */
    public Page<Product> filterByCategoryWithSort(Category category, String sortType, int page) {
        Sort sort;
        
        if ("price-asc".equals(sortType)) {
            sort = Sort.by("price").ascending();
        } else if ("price-desc".equals(sortType)) {
            sort = Sort.by("price").descending();
        } else {
            sort = Sort.by("id").ascending();
        }
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        return productRepository.findByCategory(category, pageable);
    }
    
    /**
     * Get all products with pagination and sorting
     */
    public Page<Product> getAllWithSort(String sortType, int page) {
        Sort sort;
        
        if ("price-asc".equals(sortType)) {
            sort = Sort.by("price").ascending();
        } else if ("price-desc".equals(sortType)) {
            sort = Sort.by("price").descending();
        } else {
            sort = Sort.by("id").ascending();
        }
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        return productRepository.findAll(pageable);
    }
    
    /**
     * Get page size
     */
    public int getPageSize() {
        return PAGE_SIZE;
    }
}