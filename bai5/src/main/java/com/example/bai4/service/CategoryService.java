package com.example.bai4.service;

import com.example.bai4.model.Category;
import com.example.bai4.model.Product;
import com.example.bai4.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category get(int id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.orElse(null);
    }
}