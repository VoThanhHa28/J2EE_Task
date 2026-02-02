package com.example.bai4.service;

import com.example.bai4.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private List<Product> listProduct = new ArrayList<>();

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void add(Product newProduct) {
        int maxId = listProduct.stream().mapToInt(Product::getId).max().orElse(0);
        newProduct.setId(maxId + 1);
        listProduct.add(newProduct);
    }

    public void updateImage(Product product, MultipartFile imageProduct) {
        if (imageProduct != null && !imageProduct.isEmpty()) {
            try {
                // Đường dẫn lưu file vào thư mục static/images
                Path dirImages = Paths.get("src/main/resources/static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }

                String newFileName = UUID.randomUUID().toString() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);
                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}