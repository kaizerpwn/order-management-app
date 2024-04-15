package com.ibrahimokic.ordermanagement.controller.api;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import com.ibrahimokic.ordermanagement.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to products")
public class ProductController {
    private final ProductService productService;
    private final Mapper<Product, ProductDto> productMapper;

    @GetMapping
    @Operation(summary = "Get all products", description = "Get list of all products")
    @ApiResponse(responseCode = "200", description = "List of addresses", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))
    })
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by ID", description = "Get a product by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);

        if (product.isPresent()) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Product not found.");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new product", description = "Create new product based on request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        try {
            Product newProduct = productMapper.mapFrom(productDto);
            Product createdProduct = productService.createProduct(newProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Internal server error");
        }
    }

    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit product", description = "Edit product based on request body and product ID")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto updatedProductDto) {
        return productService.updateProduct(productId, updatedProductDto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete product", description = "Delete product with provided product id")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
