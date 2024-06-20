package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class ProductRepositoryAPI implements Repository<Product> {
    private static final String BASE_URL = "https://dummyjson.com/products";
    private static final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Product> getAll() {
        String jsonResponse = restTemplate.getForObject(BASE_URL, String.class);

        JsonNode root = null;
        try {
            root = mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode data = root.at("/products");
        return mapper.convertValue(data, new TypeReference<>() {});
    }

    @Override
    public Product getById(int id) {
        return restTemplate.getForObject(BASE_URL + "/" + id, Product.class);
    }

    public List<Product> filterByCategoryAndPrice(String category, Double minPrice, Double maxPrice) {
        List<Product> products = getAll();
        return products.stream()
                .filter(product -> (category == null || product.getCategory().equals(category)) &&
                        (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice))
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
