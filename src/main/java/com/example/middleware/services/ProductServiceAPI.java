package com.example.middleware.services;

import com.example.middleware.model.Product;
import com.example.middleware.repositories.ProductRepositoryAPI;
import com.example.middleware.repositories.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceAPI {
    private final Repository<Product> repository;
    private final Logger logger = LogManager.getLogger(ProductServiceAPI.class);

    @Autowired
    public ProductServiceAPI(Repository<Product> repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return repository.getAll();
    }

    public Product getProductById(int id) {
        logger.info("Fetching product by id");
        return repository.getById(id);
    }

    @Cacheable(value = "products", key = "'filter:' + #category + ':' + #minPrice + ':' + #maxPrice")
    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice) {
        logger.info("Filtering products");
        return ((ProductRepositoryAPI) repository).filterByCategoryAndPrice(category, minPrice, maxPrice);
    }

    @Cacheable(value = "products", key = "'search: ' + #title")
    public List<Product> searchProducts(String title){
        logger.info("Searching products");
        return ((ProductRepositoryAPI) repository).searchByTitle(title);
    }
}
