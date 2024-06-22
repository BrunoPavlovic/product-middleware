package com.example.middleware.controllers;

import com.example.middleware.model.Product;
import com.example.middleware.model.UserDetails;
import com.example.middleware.services.AuthService;
import com.example.middleware.services.ProductServiceDB;
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
@RequestMapping("/api/v1/db/products")
public class ProductControllerDB {
    private final ProductServiceDB productService;
    private final AuthService authService;
    private final Logger logger = LogManager.getLogger(ProductControllerDB.class);

    @Autowired
    public ProductControllerDB(ProductServiceDB productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @Operation(summary = "Get all products",
            description = "Get all products from DB. The response is list of Products or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "500", description = "No products found!",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestHeader("Authorization") String authorizationHeader) {
        if(!isLoggedIn(authorizationHeader)){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Fetching all products from DB");
        List<Product> products =  productService.getAllProducts();
        if(!products.isEmpty()){
            logger.debug("Returning list of products from DB");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products found in DB");
        return new ResponseEntity<>("No products found in DB!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Get product by id",
            description = "Get product with given id from DB. The response is a Product or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "No product found with given id!",
                    content = @Content) })
    @Parameter( description = "Id of product to be retrieved",
            required = true)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable int id) {
        if(!isLoggedIn(authorizationHeader)){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Fetching product by id from DB");
        Product product = productService.getProductById(id);
        if(product != null){
            logger.debug("Returning product with id: {} from DB", id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }

        logger.warn("No product with id: {} found in DB", id);
        return new ResponseEntity<>("Product with id " + id + " not found in DB!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Filter products",
            description = "Filter products that match the applied filter criteria (category, minPrice, maxPrice)." +
                    " The response is list of Products or error message.")
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
    public ResponseEntity<?> filterProducts(@RequestHeader("Authorization") String authorizationHeader,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice) {
        if(!isLoggedIn(authorizationHeader)){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Filtering products in DB");
        List<Product> products = productService.filterProducts(category, minPrice, maxPrice);
        if (!products.isEmpty()) {
            logger.debug("Returning list of products from DB that matches filter");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products found in DB with given filter");
        return new ResponseEntity<>("No products found in DB with given filter!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Search products",
            description = "Search products in DB that match the provided title." +
                    " The response is list of Products or error message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "No products found with given title!",
                    content = @Content) })
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestHeader("Authorization") String authorizationHeader,
                                            @RequestParam String title){
        if(!isLoggedIn(authorizationHeader)){
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        logger.info("Searching products in DB with title: {}", title);
        List<Product> products = productService.searchProducts(title);
        if(!products.isEmpty()){
            logger.debug("Returning list of products from DB that matches given title");
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        logger.warn("No products found in DB with title: {}", title);
        return  new ResponseEntity<>("No products found in DB with given title!", HttpStatus.NOT_FOUND);
    }

    private boolean isLoggedIn(String authorizationHeader) {
        logger.info("Authorization with Bearer token");
        String token = authorizationHeader.substring("Bearer ".length());
        UserDetails user = authService.getCurrentAuthUser(token);
        return user != null;
    }
}
