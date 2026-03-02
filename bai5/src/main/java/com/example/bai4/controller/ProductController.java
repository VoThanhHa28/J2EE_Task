package com.example.bai4.controller;

import com.example.bai4.model.Product;
import com.example.bai4.repository.CategoryRepository;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService,
                             CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    // Hiển thị danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product/list";
    }

    // Form thêm
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/create";
    }

    // Xử lý thêm
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Product product,
                         BindingResult result,
                         @RequestParam("imageProduct") MultipartFile imageFile,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "product/create";
        }

        // LẤY CATEGORY TỪ DB
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            product.setCategory(
                    categoryRepository.findById(product.getCategory().getId()).orElse(null)
            );
        }

        productService.save(product);
        productService.updateImage(product, imageFile);

        return "redirect:/products";
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.get(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/edit";
    }

    // Xử lý sửa
    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id,
                         @Valid @ModelAttribute Product product,
                         BindingResult result,
                         @RequestParam("imageProduct") MultipartFile imageFile,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "product/edit";
        }

        product.setId(id);
        productService.save(product);
        productService.updateImage(product, imageFile);

        return "redirect:/products";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}