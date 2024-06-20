package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

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
        String url = BASE_URL + "/" + id;
        return restTemplate.getForObject(url, Product.class);
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
