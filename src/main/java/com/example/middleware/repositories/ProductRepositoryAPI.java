package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class ProductRepositoryAPI implements Repository<Product> {
    private static final String BASE_URL = "https://dummyjson.com/products";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = LogManager.getLogger(ProductRepositoryAPI.class);

    @Override
    public List<Product> getAll() {
        logger.info("Fetching all products from API");
        try {
            String jsonResponse = restTemplate.getForObject(BASE_URL, String.class);

            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode data = root.at("/products");

            logger.debug("Converting JSON response to list of Product objects.");
            return mapper.convertValue(data, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            logger.error("Error while fetching products: {}", e.getMessage());
            throw new RuntimeException("Error while fetching products: " + e.getMessage());
        }
    }

    @Override
    public Product getById(int id) {
        logger.info("Fetching products with id: {} from API", id);
        try {
            return restTemplate.getForObject(BASE_URL + "/" + id, Product.class);
        } catch (RestClientException e) {
            logger.error("Error while fetching product with given id: {}" , e.getMessage());
            throw new RuntimeException("Error while fetching product with given id: " + e.getMessage());
        }
    }

    public List<Product> filterByCategoryAndPrice(String category, Double minPrice, Double maxPrice) {
        logger.info("Filtering products by category: {}, minPrice: {}, maxPrice: {}", category, minPrice, maxPrice);
        List<Product> products = getAll();
        return products.stream()
                .filter(product -> (category == null || product.getCategory().equals(category)) &&
                        (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice))
                .collect(Collectors.toList());
    }

    public List<Product> searchByTitle(String title){
        logger.info("Searching products by title: {}", title);
        List<Product> products = getAll();
        return products.stream()
                .filter(product -> product.getTitle().contains(title))
                .collect(Collectors.toList());
    }

    //methods for future needs
    @Override
    public void add(Product entity) {
        //TODO:"Not yet implemented"
    }

    @Override
    public void update(Product entity) {
        //TODO:"Not yet implemented"
    }

    @Override
    public void remove(Product entity) {
        //TODO:"Not yet implemented"
    }
}
