package com.example.springmvclab.service;

import com.example.springmvclab.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        // Data dummy — nanti akan diganti database di Week JPA
        products.add(new Product(1L, "Laptop ASUS", "Elektronik", 12_500_000, 15));
        products.add(new Product(2L, "Mouse Logitech", "Elektronik", 350_000, 50));
        products.add(new Product(3L, "Buku Java Programming", "Buku", 150_000, 30));
        products.add(new Product(4L, "Kopi Arabica 250g", "Makanan", 85_000, 100));
        products.add(new Product(5L, "Headphone Sony", "Elektronik", 1_200_000, 20));
        products.add(new Product(6L, "Novel Laskar Pelangi", "Buku", 75_000, 45));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> findByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    public List<String> getAllCategories() {
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    public Map<String, Long> countByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public Product findMostExpensive() {
        return products.stream()
                .max(Comparator.comparingDouble(Product::getPrice))
                .orElseThrow();
    }

    public Product findCheapest() {
        return products.stream()
                .min(Comparator.comparingDouble(Product::getPrice))
                .orElseThrow();
    }

    public double averagePrice() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0);
    }

    public long countLowStock() {
        return products.stream()
                .filter(p -> p.getStock() < 20)
                .count();
    }
}