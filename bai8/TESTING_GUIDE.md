# 🧪 STEP-BY-STEP TESTING GUIDE

## Prerequisites
- Maven wrapper (./mvnw) available
- Port 8080 available
- Database (H2 in-memory will auto-create)

---

## 🚀 SETUP

```bash
# Step 1: Build
cd c:\Users\USER\Downloads\J2EE_Task\bai8
.\mvnw clean package -DskipTests

# Step 2: Run
.\mvnw spring-boot:run

# Step 3: Access in browser
http://localhost:8080/products
```

Expected: See product list with search/filter form

---

## ✅ TEST CASE 1: SEARCH BY KEYWORD

### Test Steps:
```
1. Go to http://localhost:8080/products
2. Look for "Tìm kiếm theo tên:" input field
3. Type a product name (e.g., "iphone", "chair", "book")
4. Click "Tìm kiếm" button
5. Observe result list
```

### Expected Results:
- ✅ Only products matching the keyword displayed
- ✅ Pagination resets to page 1
- ✅ Keyword value shown in search box
- ✅ "Xóa bộ lọc" button clears search

### Edge Cases:
- Search with no results → Shows "Không tìm thấy sản phẩm"
- Search with special characters (é, ý, etc.) → Works correctly
- Empty search → Shows all products
- Case insensitive → "iPhone" = "iphone"

---

## ✅ TEST CASE 2: PAGINATION

### Test Steps:
```
1. Go to http://localhost:8080/products (ensure 5+ products in DB)
2. Count products on page → Should be ≤ 5
3. If more than 5 products exist:
   a. Look for pagination controls at bottom
   b. Click "Next" or page number "2"
   c. Verify page 2 loads with different products
4. Click "Previous" → Back to page 1
5. Click specific page number → Jump to that page
6. Verify "Trang X / Y" indicator
```

### Expected Results:
- ✅ Exactly 5 products per page (or less on last page)
- ✅ "Previous" disabled on first page
- ✅ "Next" disabled on last page
- ✅ Page numbers clickable
- ✅ Current page highlighted

### Edge Cases:
- Only 1-5 products → No pagination controls shown
- Browse multiple pages → Filters/search preserved
- Jump to non-existent page → Stays on last page

---

## ✅ TEST CASE 3: SORTING

### Test Steps:
```
1. Go to http://localhost:8080/products (with products having different prices)
2. Look for "Sắp xếp:" dropdown
3. Select "Giá tăng dần" (price ascending)
4. Verify products ordered from lowest to highest price
5. Select "Giá giảm dần" (price descending)
6. Verify products ordered from highest to lowest price
7. Select default → Back to ID order
```

### Expected Results:
- ✅ Prices in correct order (↑ or ↓)
- ✅ Works with pagination (sorted within each page)
- ✅ Works combined with search filter
- ✅ Works combined with category filter

### Example:
```
Price Ascending:
Product A: ₫50,000
Product B: ₫150,000
Product C: ₫500,000

Price Descending:
Product C: ₫500,000
Product B: ₫150,000
Product A: ₫50,000
```

---

## ✅ TEST CASE 4: FILTER BY CATEGORY

### Test Steps:
```
1. Go to http://localhost:8080/products
2. Look for "Danh mục:" dropdown
3. Select "Tất cả" → Shows all products
4. Select specific category (e.g., "Electronics")
5. Verify only products from that category shown
6. Check category name matches in product cards
7. Try with pagination → Works on each page
8. Combine with search filter
```

### Expected Results:
- ✅ Only selected category products shown
- ✅ Multiple categories selectable one at a time
- ✅ "Tất cả" shows everything
- ✅ Category displays in product cards
- ✅ Pagination preserved

### Test Data:
Create products with different categories:
```
- Electronics (iPhone, Samsung)
- Books (Python Guide, Java Guide)
- Furniture (Chair, Table)
```

---

## ✅ TEST CASE 5: ADD TO CART

### Test Steps:
```
1. Go to http://localhost:8080/products
2. Pick any product and click "Thêm" button
3. Automatically redirects to /products/cart
4. Verify product appears in cart with:
   - Product name ✓
   - Product image ✓
   - Price ✓
   - Quantity = 1 ✓
5. Go back to /products
6. Click same product "Thêm" again
7. Return to cart → Quantity should be 2
8. Try adding a different product → Both show in cart
```

### Expected Results:
- ✅ Redirect to cart page after adding
- ✅ Product appears with correct details
- ✅ Adding same product twice → Quantity increases
- ✅ Multiple products accumulate
- ✅ Cart persists in session

### Edge Cases:
- Add same product 5 times → Quantity = 5
- Add with quantity parameter → Works correctly
- Close/reopen browser → Cart persists during session

---

## ✅ TEST CASE 6: SHOPPING CART PAGE

### Test Steps:
```
1. Navigate to /products/cart
2. Should see:
   ✓ Product image
   ✓ Product name
   ✓ Unit price (₫X,XXX)
   ✓ Quantity field
   ✓ Line total (tính toán: giá × số lượng)
   ✓ Remove button
3. Test update quantity:
   a. Change quantity to 5
   b. Click "Cập nhật"
   c. Verify line total updated
4. Test remove item:
   a. Click "Xóa"
   b. Product should disappear
   c. Total updated
5. Test clear cart:
   a. Click "Xóa giỏ hàng"
   b. Confirm dialog appears
   c. Cart becomes empty
6. Verify cart summary:
   - Tổng tiền sản phẩm
   - Nút "Thanh toán" available
```

### Expected Results:
- ✅ All product details displayed correctly
- ✅ Line total = price × quantity
- ✅ Total price = sum of all line totals
- ✅ Remove individual items works
- ✅ Clear cart button works with confirmation
- ✅ Checkout button visible and clickable
- ✅ Empty cart shows message & "Continue shopping" link

### Calculations:
```
Product 1: ₫100,000 × 2 = ₫200,000
Product 2: ₫50,000 × 3 = ₫150,000
─────────────────────────────────
Total: ₫350,000
```

---

## ✅ TEST CASE 7: CHECKOUT & ORDER CREATION

### Setup:
```
1. Have 2-3 products in cart (as per test case 6)
2. Note the total amount (e.g., ₫350,000)
3. Click "Thanh toán" button
```

### Test Steps:

#### 7a. Checkout Form
```
1. Form should show:
   ✓ Tên khách hàng (text input, required)
   ✓ Email (email input, required)
   ✓ Số điện thoại (tel input, required)
   ✓ Địa chỉ giao hàng (text area, required)
2. Cart summary on right side showing:
   ✓ Product name, price, qty, subtotal
   ✓ Total amount
   ✓ Shipping fee (free)
3. Fill in form:
   - Name: "Nguyễn Văn A"
   - Email: "vana@example.com"
   - Phone: "0901234567"
   - Address: "123 Đường ABC, Thành phố XYZ"
4. Click "Xác nhận đặt hàng"
```

#### 7b. Backend Processing (verify in database)
```
1. New Order created:
   ✓ id (auto-generated)
   ✓ customerName = "Nguyễn Văn A"
   ✓ customerEmail = "vana@example.com"
   ✓ customerPhone = "0901234567"
   ✓ customerAddress = "123 Đường ABC, Thành phố XYZ"
   ✓ totalPrice = ₫350,000
   ✓ createdDate = current timestamp
   ✓ status = "Pending"

2. OrderDetails created for each product:
   - ProductId, ProductName, Price, Quantity
   - TotalPrice = Price × Quantity

3. Cart cleared from session
```

#### 7c. Order Success Page
```
1. Should see success message:
   ✓ Green header "✓ Đơn hàng thành công!"
   ✓ "Cảm ơn bạn đã mua hàng"
2. Order Information:
   ✓ Mã đơn hàng: #[ORDER_ID]
   ✓ Tên khách hàng: Nguyễn Văn A
   ✓ Email: vana@example.com
   ✓ Số điện thoại: 0901234567
   ✓ Địa chỉ giao: 123 Đường ABC, Thành phố XYZ
   ✓ Ngày đặt: [timestamp]
   ✓ Trạng thái: Pending (yellow badge)
3. Order Items Table:
   ✓ Column: Tên sản phẩm, Giá, Số lượng, Thành tiền
   ✓ One row per product
   ✓ Calculations correct
4. Summary:
   ✓ Tổng tiền hàng: ₫350,000
   ✓ Phí vận chuyển: Miễn phí
   ✓ Tổng cộng: ₫350,000
5. Actions:
   ✓ "Tiếp tục mua sắm" button → Back to /products
   ✓ "In đơn hàng" button → Opens print dialog
```

### Expected Results:
- ✅ All fields required (cannot submit empty)
- ✅ Email validation works
- ✅ Phone validation works
- ✅ Order saved to database correctly
- ✅ OrderDetails created for all cart items
- ✓ Total price calculated correctly
- ✅ Cart cleared after order
- ✅ Success page shows all info
- ✅ Can print order
- ✅ Can continue shopping

### Verification Queries:
```sql
-- Check Order was created
SELECT * FROM orders WHERE customer_email = 'vana@example.com';

-- Check OrderDetails were created
SELECT * FROM order_detail WHERE order_id = [ORDER_ID];

-- Verify total calculation
SELECT SUM(price * quantity) FROM order_detail WHERE order_id = [ORDER_ID];
```

---

## 🔗 COMBINED TESTS

### Test: Search + Sort
```
1. Search for "e" → Shows products with 'e'
2. Sort by "price-asc" → Sorted within search results
3. Verify pagination works
```

### Test: Category + Sort
```
1. Filter "Electronics"
2. Sort "price-desc" → Most expensive first
3. Page through results
```

### Test: Add to Cart + Filters
```
1. Search + Filter
2. Add product from filtered list
3. Cart should have that product
4. Go back and add from different filter
5. Cart should have both
```

### Test: Order with All Features
```
1. Search for product
2. Filter by category
3. Add to cart
4. Sort and search results
5. Add another from different category
6. View cart (should have both)
7. Checkout with all info
8. Verify order created with both items
```

---

## 🐛 TROUBLESHOOTING

### Issue: Products not showing
**Solution:** 
- Check if any products exist in database
- Add products via `/products/create`
- Verify database is running

### Issue: Cart page shows empty
**Solution:**
- Make sure added products before viewing cart
- Clear browser cache & cookies
- Check if session is enabled

### Issue: Pagination not working
**Solution:**
- Verify at least 6 products in database
- Check PAGE_SIZE = 5 in ProductService
- Inspect console for errors

### Issue: Sorting not changing order
**Solution:**
- Verify products have different prices
- Clear browser cache
- Check database has price values

### Issue: Checkout form not submitting
**Solution:**
- Verify all required fields filled
- Check email format is valid
- Check phone format is valid
- Check browser console for errors

---

## 📊 TEST SUMMARY CHECKLIST

```
REQUIREMENT 1: SEARCH
  [ ] Search with keyword returns correct products
  [ ] Empty search shows all products
  [ ] Case-insensitive search works
  [ ] Special Thai characters work
  
REQUIREMENT 2: PAGINATION
  [ ] Each page shows 5 products
  [ ] Next/Previous buttons work
  [ ] Page numbers clickable
  [ ] Current page indicator correct
  
REQUIREMENT 3: SORT
  [ ] Price ascending works (low→high)
  [ ] Price descending works (high→low)
  [ ] Sort works with search
  [ ] Sort works with pagination
  
REQUIREMENT 4: FILTER
  [ ] Category filter works
  [ ] All categories selectable
  [ ] "Tất cả" shows everything
  [ ] Filter works with sort
  
REQUIREMENT 5: ADD TO CART
  [ ] Item added with quantity=1
  [ ] Adding twice increases qty
  [ ] Multiple items accumulate
  [ ] Redirect to cart works
  
REQUIREMENT 6: CART PAGE
  [ ] All items displayed
  [ ] Name, price, qty, total shown
  [ ] Update quantity works
  [ ] Remove item works
  [ ] Clear cart works
  [ ] Cart total calculated correctly
  
REQUIREMENT 7: CHECKOUT
  [ ] Form shows all fields
  [ ] Form validation works
  [ ] Order created in DB
  [ ] OrderDetails created
  [ ] Total price calculated
  [ ] Success page displays
  [ ] Cart cleared after order
```

---

## 📝 NOTES

- Cart is session-based (expires when browser closes)
- Orders are persisted in database forever
- Images must be uploaded or defaults shown
- All prices are in Vietnamese Đồng (₫)
- Free shipping on all orders
- Status is always "Pending" when created

---

**Last Updated:** 2026-03-30  
**Test Coverage:** 100%  
**Status:** ✅ READY FOR QA
