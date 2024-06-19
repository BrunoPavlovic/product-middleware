package com.example.middleware.repositories;

import com.example.middleware.model.Product;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductRepositoryAPI implements Repository<Product> {
    private static final String BASE_URL = "https://dummyjson.com/products";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Product> getAll() {
        Product[] products = restTemplate.getForObject(BASE_URL, Product[].class);
        if(products == null)
            return null;
        return Arrays.asList(products);
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
