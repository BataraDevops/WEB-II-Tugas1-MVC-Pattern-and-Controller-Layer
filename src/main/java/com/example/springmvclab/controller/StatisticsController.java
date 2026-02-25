package com.example.springmvclab.controller;

import com.example.springmvclab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

    private final ProductService productService;

    public StatisticsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("title", "Statistik Produk");
        model.addAttribute("totalProducts", productService.findAll().size());
        model.addAttribute("categoryCount", productService.countByCategory());
        model.addAttribute("mostExpensive", productService.findMostExpensive());
        model.addAttribute("cheapest", productService.findCheapest());
        model.addAttribute("averagePrice", productService.averagePrice());
        model.addAttribute("lowStockCount", productService.countLowStock());
        return "statistics";
    }
}