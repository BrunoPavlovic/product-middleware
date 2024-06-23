package com.example.middleware.controllers;

import com.example.middleware.model.Product;
import com.example.middleware.services.AuthorizationService;
import com.example.middleware.services.ProductServiceAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductControllerAPI.class)
public class ProductControllerAPIUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceAPI productService;

    @MockBean
    private AuthorizationService authorizationService;

    private List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp (){
        products.add(new Product(1, "Laptop", "Laptop for testing purposes", 100.0, "technology", "laptop.jpg"));
        products.add(new Product(2, "Makeup", "Makeup for making this code prettier", 200.0, "beauty", "makeup.jpg"));
    }

    @Test
    void testGetAll_statusOK() throws Exception {
        when(authorizationService.isLoggedIn(anyString())).thenReturn(true);
        when(productService.getAllProducts()).thenReturn(products);

        this.mockMvc.perform(get("/api/v1/products").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(products.size()));
    }

    @Test
    void testGetProductById_statusOK() throws Exception {
        when(authorizationService.isLoggedIn(anyString())).thenReturn(true);
        when(productService.getProductById(1)).thenReturn(products.get(0));

        mockMvc.perform(get("/api/v1/products/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void testFilterProducts_statusOK() throws Exception {
        when(authorizationService.isLoggedIn(anyString())).thenReturn(true);
        when(productService.filterProducts("beauty", 10.0, 250.0)).thenReturn(Arrays.asList(products.get(1)));

        mockMvc.perform(get("/api/v1/products/filter")
                        .header("Authorization", "Bearer token")
                        .param("category", "beauty")
                        .param("minPrice", "10.0")
                        .param("maxPrice", "250.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Makeup"));
    }

    @Test
    void testSearchProducts_statusOK() throws Exception {
        when(authorizationService.isLoggedIn(anyString())).thenReturn(true);
        when(productService.searchProducts("Laptop")).thenReturn(Arrays.asList(products.get(0)));

        mockMvc.perform(get("/api/v1/products/search")
                        .header("Authorization", "Bearer token")
                        .param("title", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Laptop"));
    }

    @Test
    void testUnauthorizedAccess_statusUnauthorized() throws Exception {
        when(authorizationService.isLoggedIn(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/v1/products")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isUnauthorized());
    }
}
