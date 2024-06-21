package com.example.middleware.controllers;

import com.example.middleware.model.Product;
import com.example.middleware.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products",
            description = "Get all products from API. The response is list of Products or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "500", description = "No products found!",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products =  productService.getAllProducts();
        if(!products.isEmpty()){
            logger.debug("Returning list of products");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products found");
        return new ResponseEntity<>("No products found!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Get product by id",
            description = "Get products with given id from API. The response is a Product or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "No product found with given id!",
                    content = @Content) })
    @Parameter( description = "Id of product to be retrieved",
            required = true)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        logger.info("Fetching product by id");
        Product product = productService.getProductById(id);
        if(product != null){
            logger.debug("Returning product with id: {}", id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        logger.warn("No products with id: {} found", id);
        return new ResponseEntity<>("Product with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get products by filter",
            description = "Get products that matches applied filter. Products can be filtered by category and price" +
                    ". The response is list of Products or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "No products found with given filter!",
                    content = @Content) })
    @Parameters({
            @Parameter(name = "category", description = "Category of the product", required = false, schema = @Schema(type = "String")),
            @Parameter(name = "minPrice", description = "Minimum price of the product", required = false, schema = @Schema(type = "integer")),
            @Parameter(name = "maxPrice", description = "Maximum price of the product", required = false, schema = @Schema(type = "integer"))
    })
    @GetMapping("/filter")
    public ResponseEntity<?> filterProducts(@RequestParam(required = false) String category,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice) {
        logger.info("Filtering products");
        List<Product> products = productService.filterProducts(category, minPrice, maxPrice);
        if (!products.isEmpty()) {
            logger.debug("Returning list of products that matches filter");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products with given filter found");
        return new ResponseEntity<>("No products found with given filter!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get products by search",
            description = "Get all products from API that matches title." +
                    " The response is list of Products or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "No products found with given title!",
                    content = @Content) })
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String title){
        logger.info("Searching products with title: {}", title);
        List<Product> products = productService.searchProducts(title);
        if(!products.isEmpty()){
            logger.debug("Returning list of products that matches given title");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products with title: {} found", title);
        return  new ResponseEntity<>("No products found with given title!", HttpStatus.NOT_FOUND);
    }
}
