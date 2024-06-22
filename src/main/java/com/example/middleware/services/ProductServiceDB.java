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
}
