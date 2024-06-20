package com.example.middleware.controllers;

import com.example.middleware.model.Product;
import com.example.middleware.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products =  productService.getAllProducts();
        if(!products.isEmpty()){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>("No products found!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        return new ResponseEntity<>("Product with id " + id + " not found!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterProducts(@RequestParam(required = false) String category,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice) {
        List<Product> products = productService.filterProducts(category, minPrice, maxPrice);
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        return new ResponseEntity<>("No products found with the given filter!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String title){
        List<Product> products = productService.searchProducts(title);
        if(!products.isEmpty()){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        return  new ResponseEntity<>("No products found with given title!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
