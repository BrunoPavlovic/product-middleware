package com.example.middleware.services;

import com.example.middleware.model.Product;
import com.example.middleware.repositories.ProductRepositoryAPI;
import com.example.middleware.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final Repository<Product> repository;

    @Autowired
    public ProductService(Repository<Product> repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.getAll();
    }

    public Product getProductById(int id) {
        return repository.getById(id);
    }

    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice) {
        return ((ProductRepositoryAPI) repository).filterByCategoryAndPrice(category, minPrice, maxPrice);
    }
}
