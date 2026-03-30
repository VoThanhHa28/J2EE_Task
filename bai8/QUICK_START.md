# 🛒 QUICK START GUIDE - E-COMMERCE BAI8

## ✅ 7 Chức Năng Đã Được Triển Khai

| # | Chức Năng | Trạng Thái | File Chính |
|---|-----------|-----------|-----------|
| 1️⃣ | Tìm kiếm theo tên | ✅ | `ProductRepository.java` |
| 2️⃣ | Phân trang (5 sản phẩm/trang) | ✅ | `ProductService.java` |
| 3️⃣ | Sắp xếp (Giá ↑/↓) | ✅ | `ProductController.java` |
| 4️⃣ | Lọc theo danh mục | ✅ | `product/list.html` |
| 5️⃣ | Thêm vào giỏ hàng | ✅ | `CartItem.java` |
| 6️⃣ | Trang giỏ hàng | ✅ | `product/cart.html` |
| 7️⃣ | Đặt hàng (Checkout) | ✅ | `Order.java` + `OrderDetail.java` |

---

## 🚀 Chạy Ứng Dụng

### Step 1: Build Project
```bash
cd c:\Users\USER\Downloads\J2EE_Task\bai8
.\mvnw clean compile
```

### Step 2: Chạy ứng dụng
```bash
.\mvnw spring-boot:run
```

### Step 3: Truy cập
```
http://localhost:8080/products
```

---

## 📱 Các URL Chính

| URL | Phương thức | Chức năng |
|-----|----------|----------|
| `/products` | GET | Danh sách sản phẩm (với search, filter, sort, pagination) |
| `/products/create` | GET/POST | Thêm sản phẩm mới |
| `/products/edit/{id}` | GET/POST | Chỉnh sửa sản phẩm |
| `/products/delete/{id}` | GET | Xóa sản phẩm |
| `/products/add-to-cart/{id}` | GET | Thêm vào giỏ hàng |
| `/products/cart` | GET | Xem giỏ hàng |
| `/products/cart/update/{id}` | POST | Cập nhật số lượng |
| `/products/cart/remove/{id}` | GET | Xóa sản phẩm khỏi giỏ |
| `/products/cart/clear` | GET | Xóa toàn bộ giỏ |
| `/products/checkout` | GET/POST | Trang thanh toán |
| `/products/order-success/{id}` | GET | Xác nhận đơn hàng |

---

## 🧪 Test Từng Tính Năng

### 1️⃣ Tìm Kiếm (Search)
```
✓ Nhập từ khóa trong ô "Tìm kiếm theo tên"
✓ Nhấn nút "Tìm kiếm"
✓ Kỳ vọng: Hiển thị sản phẩm có tên chứa từ khóa
✓ Nhấn "Xóa bộ lọc": Về trang danh sách đầy đủ
```

### 2️⃣ Phân Trang (Pagination)
```
✓ Truy cập trang sản phẩm
✓ Kiểm tra mỗi trang hiển thị đúng 5 sản phẩm
✓ Kiểm tra nút "Trước" và "Tiếp"
✓ Nhấn nút trang để nhảy qua
✓ Kiểm tra "Trang X / Y" ở dưới
```

### 3️⃣ Sắp Xếp (Sort)
```
✓ Mở dropdown "Sắp xếp"
✓ Chọn "Giá tăng dần" → Sắp xếp từ thấp → cao
✓ Chọn "Giá giảm dần" → Sắp xếp từ cao → thấp
✓ Sắp xếp kết hợp với Tìm kiếm & Phân trang
```

### 4️⃣ Lọc theo Danh Mục (Filter)
```
✓ Mở dropdown "Danh mục"
✓ Chọn một danh mục → Chỉ hiển thị sản phẩm của danh mục đó
✓ Chọn "Tất cả" → Về danh sách đầy đủ
✓ Kết hợp với Sắp xếp & Tìm kiếm
```

### 5️⃣ Thêm Vào Giỏ (Add to Cart)
```
✓ Nhấn nút "Thêm" trên sản phẩm
✓ Chuyển hướng tới trang giỏ hàng
✓ Kiểm tra sản phẩm hiện đúng với số lượng = 1
✓ Nhấn "Thêm" lần nữa → Số lượng tăng lên
✓ Có thể adjust số lượng trực tiếp
```

### 6️⃣ Xem Giỏ Hàng (View Cart)
```
✓ Nhấn nút "Giỏ hàng" ở top right
✓ Hiển thị được:
   - Hình ảnh sản phẩm
   - Tên sản phẩm
   - Giá đơn vị
   - Số lượng (có input để sửa)
   - Tổng tiền (tính = giá × số lượng)
✓ Nút "Xóa" → Xóa sản phẩm khỏi giỏ
✓ Hiển thị "Tổng tiền" của cả giỏ
✓ Nút "Thanh toán" để giỏ
✓ Nếu giỏ rỗng: Hiển thị "Giỏ hàng trống"
```

### 7️⃣ Đặt Hàng (Checkout)
```
A. Trang Checkout Form
   ✓ Điền Tên khách hàng
   ✓ Điền Email
   ✓ Điền Số điện thoại
   ✓ Điền Địa chỉ giao hàng
   ✓ Nhấn "Xác nhận đặt hàng"

B. Backend Processing
   ✓ Tạo Order mới trong DB
   ✓ Tạo OrderDetail cho mỗi sản phẩm trong giỏ
   ✓ Tính totalPrice = tổng của tất cả sản phẩm
   ✓ Xóa giỏ hàng khỏi session
   ✓ Lưu trạng thái = "Pending"

C. Trang Confirmaion (order-success)
   ✓ Hiển thị thông báo thành công
   ✓ Hiển thị mã đơn hàng (#ID)
   ✓ Hiển thị thông tin khách hàng
   ✓ Hiển thị danh sách sản phẩm & tổng tiền
   ✓ Nút "Tiếp tục mua sắm"
   ✓ Nút "In đơn hàng" (print)
```

---

## 📊 Database Schema

### Bảng: product
```
id (INT, PK) | name (VARCHAR) | price (BIGINT) | image (VARCHAR) | category_id (INT, FK)
```

### Bảng: category
```
id (INT, PK) | name (VARCHAR)
```

### Bảng: orders (MỚI)
```
id (INT, PK)
customer_name (VARCHAR, NOT NULL)
customer_email (VARCHAR, NOT NULL)
customer_phone (VARCHAR, NOT NULL)
customer_address (VARCHAR, NOT NULL)
total_price (BIGINT, NOT NULL)
created_date (DATETIME, auto)
status (VARCHAR, default='Pending')
```

### Bảng: order_detail (MỚI)
```
id (INT, PK)
order_id (INT, FK → orders.id)
product_id (INT)
product_name (VARCHAR, NOT NULL)
price (BIGINT, NOT NULL)
quantity (INT, NOT NULL)
```

---

## 🎨 UI Components

### Product List Page Features:
- ✅ Search form (keyword input)
- ✅ Category filter (dropdown)
- ✅ Sort options (price-asc, price-desc)
- ✅ Product grid (4 columns desktop, responsive)
- ✅ Add to cart button per product
- ✅ Edit/Delete buttons (admin)
- ✅ Pagination controls
- ✅ Empty state message

### Cart Page Features:
- ✅ Product list with images
- ✅ Quantity input & update button
- ✅ Remove item button
- ✅ Cart summary sidebar
- ✅ Checkout button
- ✅ Clear cart button
- ✅ Empty cart message

### Checkout Page Features:
- ✅ Customer form (name, email, phone, address)
- ✅ Order summary sidebar
- ✅ Product list with prices
- ✅ Total calculation
- ✅ Submit button

### Order Success Page Features:
- ✅ Success banner
- ✅ Order info display
- ✅ Order items table
- ✅ Price summary
- ✅ Print button
- ✅ Continue shopping button

---

## 💾 File Changes Summary

### New Files Created:
```
✅ src/main/java/com/example/bai4/model/CartItem.java
✅ src/main/java/com/example/bai4/model/Order.java
✅ src/main/java/com/example/bai4/model/OrderDetail.java
✅ src/main/java/com/example/bai4/repository/OrderRepository.java
✅ src/main/java/com/example/bai4/repository/OrderDetailRepository.java
✅ src/main/java/com/example/bai4/service/OrderService.java
✅ src/main/resources/templates/product/cart.html
✅ src/main/resources/templates/product/checkout.html
✅ src/main/resources/templates/product/order-success.html
✅ IMPLEMENTATION_GUIDE.md
```

### Updated Files:
```
✅ src/main/java/com/example/bai4/repository/ProductRepository.java
✅ src/main/java/com/example/bai4/service/ProductService.java
✅ src/main/java/com/example/bai4/controller/ProductController.java
✅ src/main/resources/templates/_layout.html
✅ src/main/resources/templates/product/list.html
```

---

## 📋 Implementation Checklist

- [x] Câu 1: Tìm kiếm theo tên
  - [x] Backend: ProductRepository query method
  - [x] Service method
  - [x] Controller endpoint
  - [x] Frontend: Search form

- [x] Câu 2: Phân trang
  - [x] PAGE_SIZE = 5 configured
  - [x] Spring Data Page/Pageable
  - [x] Frontend: Pagination controls
  - [x] Previous/Next buttons

- [x] Câu 3: Sắp xếp
  - [x] Price ascending (price-asc)
  - [x] Price descending (price-desc)
  - [x] Frontend: Sort dropdown

- [x] Câu 4: Lọc theo danh mục
  - [x] findByCategory() method
  - [x] Category filtering logic
  - [x] Frontend: Category dropdown

- [x] Câu 5: Thêm vào giỏ hàng
  - [x] CartItem model
  - [x] Session storage
  - [x] Quantity support
  - [x] Add to cart endpoint

- [x] Câu 6: Trang giỏ hàng
  - [x] Display products from session
  - [x] Show name, price, quantity, total
  - [x] Update quantity
  - [x] Remove items
  - [x] Empty state handling

- [x] Câu 7: Đặt hàng (Checkout)
  - [x] Order model with relational DB
  - [x] OrderDetail model
  - [x] Create Order from cart
  - [x] Calculate total price
  - [x] Save order details
  - [x] Checkout form
  - [x] Order success confirmation

---

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| 404 on cart/checkout | Check if templates are in correct path |
| Cart items not saving | Ensure HttpSession is configured in application |
| Images not showing | Check image folder exists: `src/main/resources/static/images` |
| Database errors | Ensure H2/MySQL is running and configured in application.properties |
| Pagination not working | Check PAGE_SIZE constant and Pageable vs List |

---

## 📝 Notes

- 🔐 Cart là session-based (không lưu vào DB)
- 📦 Order & OrderDetail được lưu vào DB
- 🔀 Sorting có 2 tùy chọn theo yêu cầu
- 📄 Mỗi trang là 5 sản phẩm (fixed)
- 🔍 Search không phân biệt hoa/thường
- ✔️ Tất cả trường checkout là bắt buộc
- 💰 Phí vận chuyển miễn phí
- ⏰ Thời gian giao hàng: 3-5 ngày

---

## 📞 Support

Nếu có lỗi quân tâm:

1. **Build error**: Check Java version (21+)
2. **Runtime error**: Check console logs
3. **UI issue**: Ensure Bootstrap & Font Awesome CDN available
4. **Database**: Ensure persistence layer configuration

---

**Version:** 1.0  
**Last Updated:** 2026-03-30  
**Status:** ✅ PRODUCTION READY
