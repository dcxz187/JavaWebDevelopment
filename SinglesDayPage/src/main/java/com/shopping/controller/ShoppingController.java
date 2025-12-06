package com.shopping.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet({"/products", "/cart", "/api/cart", "/api/products"})
public class ShoppingController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    // 商品实体类
    public static class Product {
        private int id;
        private String name;
        private double price;
        private String description;
        
        public Product() {}
        
        public Product(int id, String name, double price, String description) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
        }
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    // 购物车项实体类
    public static class CartItem {
        private Product product;
        private int quantity;
        
        public CartItem() {}
        
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }
        
        // Getters and setters
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
    
    // API响应类
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
    
    private List<Product> products;
    
    @Override
    public void init() throws ServletException {
        // 初始化商品列表
        products = new ArrayList<>();
        products.add(new Product(1, "iPhone 15", 5999.0, "最新款苹果手机"));
        products.add(new Product(2, "MacBook Pro", 12999.0, "专业级笔记本电脑"));
        products.add(new Product(3, "AirPods Pro", 1999.0, "主动降噪无线耳机"));
        products.add(new Product(4, "iPad Air", 4399.0, "轻薄平板电脑"));
        products.add(new Product(5, "Apple Watch", 2999.0, "智能手表"));
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/products":
                showProducts(request, response);
                break;
            case "/cart":
                showCart(request, response);
                break;
            case "/api/products":
                getProductsApi(request, response);
                break;
            case "/api/cart":
                getCartApi(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/cart":
                addToCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    private void showProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("products", products);
        request.getRequestDispatcher("/products.html").forward(request, response);
    }
    
    private void showCart(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        Collection<CartItem> cartItems = cart.values();
        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/cart.html").forward(request, response);
    }
    
    private void getProductsApi(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse apiResponse = new ApiResponse(true, "获取商品列表成功", products);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
    
    private void getCartApi(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        // 返回购物车信息
        Collection<CartItem> cartItems = cart.values();
        ApiResponse apiResponse = new ApiResponse(true, "获取购物车成功", cartItems);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
    
    private void addToCart(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        
        // 获取购物车
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        // 获取参数
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        // 查找商品
        Product product = null;
        for (Product p : products) {
            if (p.getId() == productId) {
                product = p;
                break;
            }
        }
        
        if (product != null) {
            // 添加到购物车
            CartItem item = cart.get(productId);
            if (item != null) {
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                cart.put(productId, new CartItem(product, quantity));
            }
        }
        
        // 重定向回商品列表
        response.sendRedirect("products");
    }
}