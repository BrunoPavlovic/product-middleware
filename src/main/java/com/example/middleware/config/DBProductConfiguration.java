package com.example.middleware.config;

import com.example.middleware.model.Product;
import com.example.middleware.repositories.ProductRepositoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBProductConfiguration implements CommandLineRunner {
    private final ProductRepositoryDB productRepository;

    @Autowired
    public DBProductConfiguration(ProductRepositoryDB productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        productRepository.save(new Product(1, "Laptop", "Laptop for testing purposes", 100.0, "technology", "laptop.jpg"));
        productRepository.save(new Product(2, "Makeup", "Makeup for making this code prettier", 200.0, "beauty", "makeup.jpg"));
    }
}
