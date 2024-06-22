package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryDB extends JpaRepository<Product, Integer> {
}
