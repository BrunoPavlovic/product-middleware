package com.example.middleware.services;

import com.example.middleware.model.Product;
import com.example.middleware.repositories.ProductRepositoryDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceDB {
    private final ProductRepositoryDB productRepository;
    private final Logger logger = LogManager.getLogger(ProductServiceAPI.class);

    @Autowired
    public ProductServiceDB(ProductRepositoryDB productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        logger.info("Fetching all products from DB");
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        logger.info("Fetching product by id from DB");
        return productRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "products", key = "'filter:' + #category + ':' + #minPrice + ':' + #maxPrice")
    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice) {
        logger.info("Filtering products from DB");
        return productRepository.findAll().stream()
                .filter(product -> (category == null || product.getCategory().equals(category)) &&
                        (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice))
                .toList();
    }

    @Cacheable(value = "products", key = "'search: ' + #title")
    public List<Product> searchProducts(String title){
        logger.info("Searching products from DB");
        return productRepository.findAll().stream()
                .filter(product -> product.getTitle().contains(title))
                .toList();
    }
}
