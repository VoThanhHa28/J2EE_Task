package com.example.bai4.controller;

import com.example.bai4.model.*;
import com.example.bai4.repository.CategoryRepository;
import com.example.bai4.service.OrderService;
import com.example.bai4.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService,
                             OrderService orderService,
                             CategoryRepository categoryRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryRepository = categoryRepository;
    }

    // ===== PRODUCT LIST WITH SEARCH, FILTER, SORT, PAGINATION =====
    
    /**
     * Display product list with search, filter, sort, and pagination
     */
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "-1") int categoryId,
            @RequestParam(defaultValue = "") String sort,
            Model model) {
        
        Page<Product> products;
        
        // Determine which filtering/sorting method to use
        if (categoryId > 0) {
            // Filter by category
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                if (sort.isEmpty()) {
                    products = productService.filterByCategory(category, page);
                } else {
                    products = productService.filterByCategoryWithSort(category, sort, page);
                }
            } else {
                products = productService.searchWithSort(keyword, sort, page);
            }
        } else if (!keyword.isEmpty()) {
            // Search by keyword
            products = productService.searchWithSort(keyword, sort, page);
        } else {
            // Get all products
            if (sort.isEmpty()) {
                products = productService.searchByKeyword("", page);
            } else {
                products = productService.getAllWithSort(sort, page);
            }
        }
        
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAll());
        
        return "product/list";
    }

    // ===== PRODUCT MANAGEMENT (Create, Edit, Delete) =====
    
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
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            product.setCategory(
                    categoryRepository.findById(product.getCategory().getId()).orElse(null)
            );
        }
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

    // ===== SHOPPING CART FUNCTIONALITY =====
    
    /**
     * Add product to cart (stored in session)
     */
    @GetMapping("/add-to-cart/{id}")
    public String addToCart(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {
        
        Product product = productService.get(id);
        if (product == null) {
            return "redirect:/products";
        }
        
        // Get cart from session or create new one
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cartMap == null) {
            cartMap = new LinkedHashMap<>();
            session.setAttribute("cart", cartMap);
        }
        
        // Add or update item in cart
        if (cartMap.containsKey(id)) {
            CartItem item = cartMap.get(id);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setImage(product.getImage());
            cartMap.put(id, item);
        }
        
        return "redirect:/products/cart";
    }
    
    /**
     * View shopping cart
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        
        if (cartMap == null || cartMap.isEmpty()) {
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalPrice", 0L);
            return "product/cart";
        }
        
        List<CartItem> cartItems = new ArrayList<>(cartMap.values());
        long totalPrice = cartItems.stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartSize", cartMap.size());
        
        return "product/cart";
    }
    
    /**
     * Update cart item quantity
     */
    @PostMapping("/cart/update/{id}")
    public String updateCart(
            @PathVariable int id,
            @RequestParam int quantity,
            HttpSession session) {
        
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        
        if (cartMap != null && cartMap.containsKey(id)) {
            if (quantity <= 0) {
                cartMap.remove(id);
            } else {
                cartMap.get(id).setQuantity(quantity);
            }
            session.setAttribute("cart", cartMap);
        }
        
        return "redirect:/products/cart";
    }
    
    /**
     * Remove item from cart
     */
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable int id, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        
        if (cartMap != null) {
            cartMap.remove(id);
            session.setAttribute("cart", cartMap);
        }
        
        return "redirect:/products/cart";
    }
    
    /**
     * Clear entire cart
     */
    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/products/cart";
    }

    // ===== CHECKOUT AND ORDER MANAGEMENT =====
    
    /**
     * Show checkout form
     */
    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        
        if (cartMap == null || cartMap.isEmpty()) {
            return "redirect:/products/cart";
        }
        
        List<CartItem> cartItems = new ArrayList<>(cartMap.values());
        long totalPrice = cartItems.stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
        
        model.addAttribute("order", new Order());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        
        return "product/checkout";
    }
    
    /**
     * Process checkout and create order
     */
    @PostMapping("/checkout")
    public String processCheckout(
            @ModelAttribute Order order,
            HttpSession session,
            Model model) {
        
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartMap = (Map<Integer, CartItem>) session.getAttribute("cart");
        
        if (cartMap == null || cartMap.isEmpty()) {
            return "redirect:/products/cart";
        }
        
        try {
            List<CartItem> cartItems = new ArrayList<>(cartMap.values());
            
            // Create order
            Order createdOrder = orderService.createOrder(order, cartItems);
            
            // Clear cart
            session.removeAttribute("cart");
            
            // Redirect to order success page
            return "redirect:/products/order-success/" + createdOrder.getId();
            
        } catch (Exception e) {
            List<CartItem> cartItems = new ArrayList<>(cartMap.values());
            long totalPrice = cartItems.stream()
                    .mapToLong(CartItem::getTotalPrice)
                    .sum();
            
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("error", "Có lỗi khi tạo đơn hàng. Vui lòng thử lại.");
            
            return "product/checkout";
        }
    }
    
    /**
     * Order success page
     */
    @GetMapping("/order-success/{id}")
    public String orderSuccess(@PathVariable int id, Model model) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            return "redirect:/products";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", order.getOrderDetails());
        
        return "product/order-success";
    }
}