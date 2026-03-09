package com.example.bai4.service;

import com.example.bai4.model.Product;
import com.example.bai4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

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
}