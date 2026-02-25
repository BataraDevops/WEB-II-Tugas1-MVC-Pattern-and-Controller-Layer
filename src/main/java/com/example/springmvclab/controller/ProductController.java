package com.example.springmvclab.controller;

import com.example.springmvclab.model.Product;
import com.example.springmvclab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection (Week 3 pattern)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /products → tampilkan semua produk
    @GetMapping
    public String listProducts(Model model) {  // ← Model object (Baki Pengantar)
        List<Product> products = productService.findAll();  // ← dari Service (Model Layer/Dapur)

        model.addAttribute("products", products);       // ← taruh Data Class di Baki
        model.addAttribute("title", "Daftar Produk");
        model.addAttribute("totalProducts", products.size());

        return "product/list";  // → templates/product/list.html
    }

    // GET /products/42 → tampilkan detail produk
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("title", "Detail: " + product.get().getName());
        } else {
            model.addAttribute("error", "Produk dengan ID " + id + " tidak ditemukan");
            model.addAttribute("title", "Produk Tidak Ditemukan");
        }

        return "product/detail";  // → templates/product/detail.html
    }

    // GET /products/category/Elektronik → filter by category
    @GetMapping("/category/{category}")
    public String productsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.findByCategory(category);

        model.addAttribute("products", products);
        model.addAttribute("title", "Kategori: " + category);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("selectedCategory", category);

        return "product/list";  // reuse template yang sama!
    }

    // GET /products/search?keyword=laptop → search produk
    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "") String keyword,
                                 Model model) {
        List<Product> products = keyword.isBlank()
                ? productService.findAll()
                : productService.search(keyword);

        model.addAttribute("products", products);
        model.addAttribute("title", "Hasil Pencarian: " + keyword);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("keyword", keyword);

        return "product/list";  // reuse template yang sama!
    }

    @GetMapping("/categories")
    public String categorySummary(Model model) {
        Map<String, Long> categoryCount = productService.countByCategory();
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("title", "Semua Kategori");
        return "product/categories";
    }
}