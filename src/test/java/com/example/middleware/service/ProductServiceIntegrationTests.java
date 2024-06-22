package com.example.middleware.service;

import com.example.middleware.model.Product;
import com.example.middleware.repositories.ProductRepositoryAPI;
import com.example.middleware.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(ProductRepositoryAPI.class)
public class ProductServiceIntegrationTests {
    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @Mock
    private ProductRepositoryAPI productRepositoryAPI;

    @InjectMocks
    private ProductService productServiceMock;

    @Test
    void testGetAllProducts_Success() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(30, products.size());
    }

    @Test
    void testGetProductById_Success() {
        Product product = productService.getProductById(1);
        assertNotNull(product);
        assertEquals(1, product.getId());
    }

    @Test
    void testFilterProducts_Success() {
        List<Product> products = productService.filterProducts("beauty", 2.0, 20.0);
        assertNotNull(products);
        assertTrue(products.stream().allMatch(product -> product.getCategory().equals("beauty")));
        assertTrue(products.stream().allMatch(product -> product.getPrice() >= 2.0 && product.getPrice() <= 20.0));
    }

    @Test
    void testSearchProducts_Success() {
        List<Product> products = productService.searchProducts("Esse");
        assertNotNull(products);
        assertTrue(products.stream().allMatch(product -> product.getTitle().contains("Esse")));
    }

    @Test
    void testCaching_Success() {
        productService.searchProducts("Esse");
        assertNotNull(cacheManager.getCache("products").get("search: Esse"));
    }
}
