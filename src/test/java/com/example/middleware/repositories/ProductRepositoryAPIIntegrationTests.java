package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductRepositoryAPIIntegrationTests {
    @InjectMocks
    private ProductRepositoryAPI productRepositoryAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll_Success(){
        List<Product> actualProducts = productRepositoryAPI.getAll();
        assertEquals(30, actualProducts.size());
    }

    @Test
    void testGetAll_Error() {
        try {
            productRepositoryAPI.getAll();
        } catch (RuntimeException e) {
            assertEquals("Error while fetching products: Error while fetching data from API", e.getMessage());
        }
    }

    @Test
    void testGetById_Success() {
        Product actualProduct = productRepositoryAPI.getById(1);
        assertEquals(1, actualProduct.getId());
        assertEquals("Essence Mascara Lash Princess", actualProduct.getTitle());
    }

    @Test
    void testGetById_NotFound() {
        try {
            productRepositoryAPI.getById(999);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Error while fetching product with given id ->"));
        }
    }

    @Test
    void testFilterByCategoryAndPrice() {
        List<Product> filteredProducts = productRepositoryAPI.filterByCategoryAndPrice("beauty", 2.0, 20.0);

        assertEquals(5, filteredProducts.size());
        assertEquals("Essence Mascara Lash Princess", filteredProducts.get(0).getTitle());
    }

    @Test
    void testSearchByTitle(){
        List<Product> searchedProducts = productRepositoryAPI.searchByTitle("Esse");

        assertEquals(1, searchedProducts.size());
        assertEquals(1, searchedProducts.get(0).getId());
    }
}
