package com.example.bai4.controller;

import com.example.bai4.model.Product;
import com.example.bai4.service.CategoryService;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") Product newProduct,
                         BindingResult result,
                         @RequestParam("imageProduct") MultipartFile imageProduct,
                         @RequestParam("category.id") int categoryId,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        // Xử lý upload ảnh
        productService.updateImage(newProduct, imageProduct);

        // Gán Category cho Product
        newProduct.setCategory(categoryService.get(categoryId));

        productService.add(newProduct);
        return "redirect:/products";
    }
}