# Tokonyadia API
This is documentation about tokonyadia API.

## Technology
Stack that i used for this project:
- Java 17
- Springboot
- Spring Security
- JWT (Access Token)
- Redis
- PostgreSQL


## Table of Contents
- [File Management](#file-management)
- [Authentication](#authentication)
- [User Management](#user-management)
- [Seller Management](#seller-management)
- [Payment](#payment)
- [Product Categories](#product-categories)
- [Customer Management](#customer-management)
- [Order](#order)
- [Store Management](#store-management)
- [Product Management](#product-management)

---

## File Management
APIs for file handling operations.

### **GET** `/api/images/{id}`
Download an image by its ID.

---

## Authentication
APIs for user authentication, registration, and token management.

### **POST** `/api/auth/register`
Register a new user.

### **POST** `/api/auth/refresh-token`
Refresh an access token.

### **POST** `/api/auth/logout`
Log out the currently authenticated user.

### **POST** `/api/auth/login`
Log in an existing user.

---

## User Management
APIs for managing user accounts.

### **POST** `/api/users`
Create a new user.

### **PATCH** `/api/users/{id}/update-password`
Update a user's password.

### **GET** `/api/users/me`
Retrieve the authenticated user's information.

---

## Seller Management
APIs for managing seller data.

### **GET** `/api/seller/{id}`
Retrieve seller details by ID.

### **PUT** `/api/seller/{id}`
Update seller details.

### **DELETE** `/api/seller/{id}`
Delete a seller by ID.

### **GET** `/api/seller`
Retrieve all sellers.

### **POST** `/api/seller`
Create a new seller.

---

## Payment
APIs for managing payments.

### **POST** `/api/payments`
Create a new payment.

### **POST** `/api/payments/notifications`
Handle payment notifications.

### **GET** `/api/payments/{orderId}/status`
Retrieve payment status by order ID.

---

## Product Categories
APIs for managing product categories.

### **PUT** `/api/products/categories/{id}`
Update an existing product category.

### **DELETE** `/api/products/categories/{id}`
Delete a product category.

### **GET** `/api/products/categories`
Retrieve all product categories.

### **POST** `/api/products/categories`
Create a new product category.

---

## Customer Management
APIs for managing customer data.

### **GET** `/api/customers/{id}`
Retrieve customer details by ID.

### **PUT** `/api/customers/{id}`
Update customer details.

### **DELETE** `/api/customers/{id}`
Delete a customer by ID.

### **GET** `/api/customers`
Retrieve all customers.

### **POST** `/api/customers`
Create a new customer.

---

## Order
APIs for managing orders and order details.

### **PUT** `/api/orders/{orderId}/details/{detailsId}`
Update order detail by ID.

### **DELETE** `/api/orders/{orderId}/details/{detailsId}`
Remove an order detail.

### **GET** `/api/orders/{orderId}/details`
Retrieve order details by order ID.

### **POST** `/api/orders/{orderId}/details`
Add a detail to an order.

### **POST** `/api/orders/draft`
Create a draft order.

### **GET** `/api/orders/{orderId}`
Retrieve an order by ID.

### **PATCH** `/api/orders/{orderId}`
Update order status.

### **GET** `/api/orders`
Retrieve all orders.

---

## Store Management
APIs for managing store data.

### **GET** `/api/stores/{id}`
Retrieve store details by ID.

### **PUT** `/api/stores/{id}`
Update store details.

### **DELETE** `/api/stores/{id}`
Delete a store by ID.

### **GET** `/api/stores`
Retrieve all stores.

### **POST** `/api/stores`
Create a new store.

---

## Product Management
APIs for managing products and their images.

### **GET** `/api/products/{id}`
Retrieve product details by ID.

### **PUT** `/api/products/{id}`
Update product details.

### **DELETE** `/api/products/{id}`
Delete a product.

### **GET** `/api/products`
Retrieve all products.

### **POST** `/api/products`
Create a new product.

### **DELETE** `/api/products/images/{id}`
Delete a specific product image.

### **PATCH** `/api/products/images/{id}`
Update a specific product image.

### **GET** `/api/products/stores`
Retrieve all products associated with a store.

---

## Notes
- Each endpoint may require authentication using tokens.
- Ensure proper error handling and validation for all requests.
- For more details on request and response structures, refer to the full API specification.
