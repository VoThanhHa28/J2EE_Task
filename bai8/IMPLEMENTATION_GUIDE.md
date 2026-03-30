# HƯỚNG DẪN TRIỂN KHAI E-COMMERCE - BAI8

## 📋 TỔNG QUAN CÁC CHỨC NĂNG ĐÃ TRIỂN KHAI

Tất cả 7 yêu cầu đã được hoàn toàn triển khai:

---

## ✅ CÂU 1: TÌM KIẾM SẢN PHẨM THEO TÊN (1 ĐIỂM)

### 📝 Yêu cầu
- Nhập keyword từ form
- Hiển thị kết quả đúng

### 🛠️ Triển khai
**Backend:**
- **ProductRepository.java**: Thêm phương thức tìm kiếm
  ```java
  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
  ```

- **ProductService.java**: Thêm phương thức business logic
  ```java
  public Page<Product> searchByKeyword(String keyword, int page)
  public Page<Product> searchWithSort(String keyword, String sortType, int page)
  ```

- **ProductController.java**: Thêm tham số `keyword` vào endpoint `/products`
  ```java
  @GetMapping
  public String list(@RequestParam(defaultValue = "") String keyword, ...)
  ```

**Frontend:**
- **product/list.html**: Thêm form tìm kiếm
  ```html
  <input type="text" id="keyword" name="keyword" placeholder="Nhập tên sản phẩm...">
  ```

---

## ✅ CÂU 2: PHÂN TRANG (PAGINATION) (1 ĐIỂM)

### 📝 Yêu cầu
- Mỗi trang hiển thị 5 sản phẩm
- Có nút Next / Previous

### 🛠️ Triển khai
**Backend:**
- **ProductService.java**: Đặt PAGE_SIZE = 5
  ```java
  private static final int PAGE_SIZE = 5;
  ```

- **ProductController.java**: Sử dụng Spring Data `Page<Product>` và `Pageable`
  ```java
  @RequestParam(defaultValue = "0") int page
  Page<Product> products
  ```

**Frontend:**
- **product/list.html**: Thêm nút Previous/Next và số trang
  ```html
  <!-- Previous button -->
  <a th:if="${currentPage > 0}" th:href="@{/products(page=${currentPage - 1}...">
    <i class="fas fa-chevron-left"></i> Trước
  </a>
  
  <!-- Page buttons -->
  <a th:each="pageNum : ${#numbers.sequence(0, totalPages - 1)}"...>
  
  <!-- Next button -->
  <a th:if="${currentPage < totalPages - 1}" th:href="@{/products(page=${currentPage + 1}...">
    Tiếp <i class="fas fa-chevron-right"></i>
  </a>
  ```

---

## ✅ CÂU 3: SẮP XẾP (SORT) (1 ĐIỂM)

### 📝 Yêu cầu
- Theo giá tăng dần
- Theo giá giảm dần

### 🛠️ Triển khai
**Backend:**
- **ProductService.java**: Thêm hỗ trợ sắp xếp
  ```java
  public Page<Product> searchWithSort(String keyword, String sortType, int page)
  public Page<Product> filterByCategoryWithSort(Category category, String sortType, int page)
  public Page<Product> getAllWithSort(String sortType, int page)
  ```

  Logic sắp xếp:
  - `sort == "price-asc"` → Sắp xếp giá từ thấp đến cao
  - `sort == "price-desc"` → Sắp xếp giá từ cao đến thấp

**Frontend:**
- **product/list.html**: Thêm dropdown sắp xếp
  ```html
  <select id="sort" name="sort">
    <option value="">Mặc định</option>
    <option value="price-asc">Giá tăng dần</option>
    <option value="price-desc">Giá giảm dần</option>
  </select>
  ```

---

## ✅ CÂU 4: LỌC THEO DANH MỤC (CATEGORY) (1 ĐIỂM)

### 📝 Yêu cầu
- Dropdown chọn category
- Hiển thị sản phẩm theo category

### 🛠️ Triển khai
**Backend:**
- **ProductRepository.java**: Thêm phương thức lọc theo category
  ```java
  Page<Product> findByCategory(Category category, Pageable pageable);
  ```

- **ProductService.java**: Thêm phương thức business logic
  ```java
  public Page<Product> filterByCategory(Category category, int page)
  public Page<Product> filterByCategoryWithSort(Category category, String sortType, int page)
  ```

- **ProductController.java**: Xử lý tham số `categoryId`
  ```java
  @RequestParam(defaultValue = "-1") int categoryId
  ```

**Frontend:**
- **product/list.html**: Thêm dropdown danh mục
  ```html
  <select id="categoryId" name="categoryId">
    <option value="-1">Tất cả</option>
    <option th:each="cat : ${categories}" th:value="${cat.id}" th:text="${cat.name}"></option>
  </select>
  ```

---

## ✅ CÂU 5: THÊM VÀO GIỎ HÀNG (ADD TO CART) (1 ĐIỂM)

### 📝 Yêu cầu
- Lưu vào session
- Có quantity

### 🛠️ Triển khai
**Models:**
- **CartItem.java** (NEW): 
  ```java
  - productId: Integer
  - productName: String
  - price: Long
  - quantity: Integer
  - image: String
  - getTotalPrice(): Long
  ```
  Implements Serializable để lưu trong session

**Backend:**
- **ProductController.java**: Thêm endpoint `/products/add-to-cart/{id}`
  ```java
  @GetMapping("/add-to-cart/{id}")
  public String addToCart(@PathVariable int id, @RequestParam(defaultValue = "1") int quantity, HttpSession session)
  ```
  
  Logic:
  1. Lấy sản phẩm từ DB
  2. Lấy giỏ hàng từ session (hoặc tạo mới)
  3. Nếu sản phẩm đã có → cộng số lượng
  4. Nếu chưa có → thêm sản phẩm mới
  5. Lưu giỏ hàng vào session

**Frontend:**
- **product/list.html**: Thêm nút "Thêm vào giỏ hàng"
  ```html
  <a th:href="@{/products/add-to-cart/{id}(id=${product.id})}" class="btn btn-sm btn-success">
    <i class="fas fa-cart-plus"></i> Thêm
  </a>
  ```

---

## ✅ CÂU 6: TRANG GIỎ HÀNG (1 ĐIỂM)

### 📝 Yêu cầu
- Hiển thị danh sách sản phẩm đã chọn
- Hiển thị: tên, giá, số lượng, tổng tiền

### 🛠️ Triển khai
**Backend:**
- **ProductController.java**: Thêm các endpoint
  ```java
  @GetMapping("/cart")
  public String viewCart(HttpSession session, Model model)
  
  @PostMapping("/cart/update/{id}")
  public String updateCart(@PathVariable int id, @RequestParam int quantity, HttpSession session)
  
  @GetMapping("/cart/remove/{id}")
  public String removeFromCart(@PathVariable int id, HttpSession session)
  
  @GetMapping("/cart/clear")
  public String clearCart(HttpSession session)
  ```

**Frontend:**
- **product/cart.html** (NEW): 
  ```html
  - Hiển thị danh sách sản phẩm trong giỏ
  - Mỗi sản phẩm hiển thị:
    * Hình ảnh
    * Tên sản phẩm
    * Giá
    * Input điều chỉnh số lượng
    * Nút cập nhật / xóa
    * Tính toán tổng tiền
  - Phía bên phải: Tóm tắt với tổng tiền, nút thanh toán
  - Nếu giỏ hàng trống: Hiển thị thông báo
  ```

---

## ✅ CÂU 7: THANH TOÁN VÀ ĐẶT HÀNG (CHECKOUT) (1 ĐIỂM)

### 📝 Yêu cầu
- Tạo Order
- Lưu Order Detail
- Tính tổng tiền

### 🛠️ Triển khai
**Models:**
- **Order.java** (NEW):
  ```java
  - id: Integer (PK)
  - customerName: String (REQUIRED)
  - customerEmail: String (REQUIRED)
  - customerPhone: String (REQUIRED)
  - customerAddress: String (REQUIRED)
  - totalPrice: Long (REQUIRED)
  - createdDate: LocalDateTime (auto-populated)
  - status: String (default: "Pending")
  - orderDetails: List<OrderDetail> (one-to-many)
  ```

- **OrderDetail.java** (NEW):
  ```java
  - id: Integer (PK)
  - order: Order (FK, many-to-one)
  - productId: Integer
  - productName: String
  - price: Long
  - quantity: Integer
  - getTotalPrice(): Long
  ```

**Repositories:**
- **OrderRepository.java** (NEW): JpaRepository<Order, Integer>
- **OrderDetailRepository.java** (NEW): JpaRepository<OrderDetail, Integer>

**Services:**
- **OrderService.java** (NEW):
  ```java
  @Transactional
  public Order createOrder(Order order, List<CartItem> cartItems)
    1. Tính tổng tiền từ cartItems
    2. Save Order vào DB
    3. Tạo OrderDetail cho mỗi CartItem
    4. Lưu tất cả OrderDetail vào DB
    5. Return Order
  ```

**Backend:**
- **ProductController.java**: Thêm endpoints
  ```java
  @GetMapping("/checkout")
  public String checkoutForm(HttpSession session, Model model)
  
  @PostMapping("/checkout")
  public String processCheckout(@ModelAttribute Order order, HttpSession session, Model model)
    1. Lấy cart từ session
    2. Gọi orderService.createOrder()
    3. Xóa cart khỏi session
    4. Redirect đến order-success page
  
  @GetMapping("/order-success/{id}")
  public String orderSuccess(@PathVariable int id, Model model)
  ```

**Frontend:**
- **product/checkout.html** (NEW):
  ```html
  - Phía bên trái: Form nhập thông tin khách hàng
    * Tên khách hàng (required)
    * Email (required)
    * Số điện thoại (required)
    * Địa chỉ giao hàng (required)
    * Nút "Xác nhận đặt hàng"
  
  - Phía bên phải: Tóm tắt đơn hàng
    * Danh sách sản phẩm với giá, số lượng
    * Tổng tiền
    * Phí vận chuyển (miễn phí)
  ```

- **product/order-success.html** (NEW):
  ```html
  - Đầu trang: Thông báo thành công (màu xanh)
  - Thông tin đơn hàng:
    * Mã đơn hàng
    * Tên, email, SĐT, địa chỉ khách hàng
    * Ngày đặt
    * Trạng thái (Pending)
  - Chi tiết sản phẩm (bảng):
    * Tên sản phẩm, giá, số lượng, thành tiền
  - Tóm tắt:
    * Tổng tiền hàng
    * Phí vận chuyển
    * Tổng cộng
  - Nút: Tiếp tục mua sắm, In đơn hàng
  ```

---

## 📦 CẤU TRÚC DỰ ÁN

```
bai8/
├── src/main/java/com/example/bai4/
│   ├── controller/
│   │   └── ProductController.java (CẬP NHẬT)
│   ├── model/
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── CartItem.java (MỚI)
│   │   ├── Order.java (MỚI)
│   │   └── OrderDetail.java (MỚI)
│   ├── repository/
│   │   ├── ProductRepository.java (CẬP NHẬT)
│   │   ├── OrderRepository.java (MỚI)
│   │   └── OrderDetailRepository.java (MỚI)
│   ├── service/
│   │   ├── ProductService.java (CẬP NHẬT)
│   │   └── OrderService.java (MỚI)
│   └── Bai5Application.java
├── src/main/resources/
│   ├── templates/
│   │   ├── _layout.html (CẬP NHẬT)
│   │   └── product/
│   │       ├── list.html (CẬP NHẬT)
│   │       ├── create.html
│   │       ├── edit.html
│   │       ├── cart.html (MỚI)
│   │       ├── checkout.html (MỚI)
│   │       └── order-success.html (MỚI)
│   └── application.properties
└── pom.xml
```

---

## 🔗 WORKFLOW NGƯỜI DÙNG

### 1️⃣ Tìm kiếm & Duyệt sản phẩm
```
GET /products
├── Tìm kiếm theo tên (keyword)
├── Lọc theo danh mục (categoryId)
├── Sắp xếp theo giá (sort: price-asc/price-desc)
├── Phân trang (page: 0,1,2...)
└── Mỗi trang hiển thị 5 sản phẩm
```

### 2️⃣ Thêm vào giỏ hàng
```
GET /products/add-to-cart/{id}
├── Lấy sản phẩm từ DB
├── Lưu vào session (Map<productId, CartItem>)
└── Điều chỉnh số lượng (quantity param)
```

### 3️⃣ Xem giỏ hàng
```
GET /products/cart
├── Hiển thị tất cả CartItem  
├── Cho phép cập nhật số lượng (POST /products/cart/update/{id})
├── Cho phép xóa sản phẩm (GET /products/cart/remove/{id})
├── Cho phép xóa toàn bộ giỏ (GET /products/cart/clear)
└── Hiển thị tổng tiền
```

### 4️⃣ Thanh toán
```
GET  /products/checkout (Form)
     ↓
POST /products/checkout (Submit)
├── Lưu Order vào DB (với totalPrice tính toán)
├── Lưu OrderDetail cho mỗi CartItem vào DB
├── Xóa giỏ hàng khỏi session
└── Redirect đến order success page

GET /products/order-success/{id}
└── Hiển thị xác nhận đơn hàng
```

---

## 🧪 KIỂM THỬ

### Test Search (Câu 1)
```
1. Truy cập http://localhost:8080/products
2. Nhập từ khóa vào ô "Tìm kiếm theo tên"
3. Kỳ vọng: Hiển thị sản phẩm có tên chứa từ khóa
```

### Test Pagination (Câu 2)
```
1. Truy cập http://localhost:8080/products
2. Kiểm tra danh sách hiển thị đúng 5 sản phẩm/trang
3. Kỳ vọng: Nút Previous/Next hoạt động, chuyển trang được
```

### Test Sort (Câu 3)
```
1. Truy cập http://localhost:8080/products
2. Chọn "Giá tăng dần" → Sản phẩm sắp xếp từ giá thấp đến cao
3. Chọn "Giá giảm dần" → Sản phẩm sắp xếp từ giá cao đến thấp
```

### Test Filter by Category (Câu 4)
```
1. Truy cập http://localhost:8080/products
2. Chọn một danh mục từ dropdown "Danh mục"
3. Kỳ vọng: Chỉ hiển thị sản phẩm của danh mục đó
```

### Test Add to Cart (Câu 5)
```
1. Truy cập http://localhost:8080/products
2. Nhấn nút "Thêm" trên sản phẩm
3. Xem giỏ hàng → Sản phẩm xuất hiện với số lượng = 1
4. Nhấn "Thêm" lần 2 → Số lượng tăng lên 2
```

### Test Cart Page (Câu 6)
```
1. Truy cập http://localhost:8080/products/cart
2. Kỳ vọng: Hiển thị tên, giá, số lượng, tổng tiền mỗi sản phẩm
3. Có thể cập nhật số lượng, xóa sản phẩm
4. Hiển thị tổng tiền cả giỏ
```

### Test Checkout (Câu 7)
```
1. Có sản phẩm trong giỏ hàng
2. Nhấn nút "Thanh toán"
3. Điền form: Tên, Email, SĐT, Địa chỉ
4. Nhấn "Xác nhận đặt hàng"
5. Kỳ vọng:
   - Tạo Order vào DB
   - Tạo OrderDetail cho mỗi sản phẩm
   - Tính tổng tiền chính xác
   - Hiển thị trang xác nhận với thông tin đơn hàng
   - Giỏ hàng được xóa
```

---

## 📱 RESPONSIVE DESIGN

Tất cả các trang được thiết kế responsive:
- Desktop: Grid layout với 4 cột sản phẩm
- Tablet: Grid layout với 2-3 cột
- Mobile: Grid layout với 1 cột

CSS media queries được thêm cho:
- checkout-container
- cart-container
- product-grid

---

## 🎨 UI/UX IMPROVEMENTS

✨ **Thêm các cải tiến giao diện:**
- Biểu tượng Font Awesome trên tất cả các nút
- Grid layout hiện đại cho sản phẩm (thay vì bảng)
- Form filter đầy đủ và dễ sử dụng
- Thông báo empty state (giỏ hàng rỗng)
- Sticky cart summary khi cuộn
- Tóm tắt đơn hàng tại checkout
- Trang xác nhận đẹp mắt

---

## ✅ CHECKLIST CÀI ĐẶT

Để chạy dự án:

1. **Build project**
   ```bash
   cd c:\Users\USER\Downloads\J2EE_Task\bai8
   .\mvnw clean compile
   ```

2. **Chạy ứng dụng**
   ```bash
   .\mvnw spring-boot:run
   ```

3. **Truy cập**
   - URL: http://localhost:8080/products
   - HĐH sẽ tạo bảng tự động (JPA)

4. **Thêm dữ liệu mẫu** (optional)
   - Truy cập `/products/create`
   - Thêm category và sản phẩm

---

## 📝 NOTES

- CartItem được lưu trong `HttpSession` (không lưu vào DB)
- Order và OrderDetail được lưu vào database
- Sắp xếp chỉ có 2 loại (price-asc, price-desc) theo yêu cầu
- Phân trang cố định 5 sản phẩm/trang
- Tính năng tìm kiếm không phân biệt hoa/thường (ignoreCase)
- Tất cả thông tin input ở checkout là bắt buộc (required)

---

**Created: 2026-03-30**
**Status: ✅ COMPLETED**
