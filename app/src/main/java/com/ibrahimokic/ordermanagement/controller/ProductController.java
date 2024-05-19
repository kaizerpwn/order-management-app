package com.ibrahimokic.ordermanagement.controller;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.mapper.impl.ProductMapperImpl;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.utils.Utils;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapperImpl productMapper;

    @GetMapping
    @Operation(summary = "Get all products", description = "Get list of all products")
    @ApiResponse(responseCode = "200", description = "List of addresses", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))
    })
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductDto> retrievedProducts = productMapper.mapListToDtoList(productService.getAllProducts());
            return ResponseEntity.ok(retrievedProducts);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by ID", description = "Get a product by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            Optional<Product> product = productService.getProductById(productId);

            if (product.isPresent()) {
                return ResponseEntity.ok(productMapper.mapTo(product.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("Product not found.");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
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
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto, BindingResult bindingResult) {
        ResponseEntity<?> errors = Utils.getBindingResults(bindingResult);
        if (errors != null)
            return errors;

        try {
            Product newProduct = productMapper.mapFrom(productDto);
            Product createdProduct = productService.createProduct(newProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.mapTo(createdProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit product", description = "Edit product based on request body and product ID")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
            @Valid @RequestBody ProductDto updatedProductDto, BindingResult bindingResult) {
        ResponseEntity<?> errors = Utils.getBindingResults(bindingResult);
        if (errors != null)
            return errors;

        try {
            Optional<Product> updatedProduct = productService.updateProduct(productId, updatedProductDto);

            if (updatedProduct.isPresent()) {
                return ResponseEntity.ok().body(productMapper.mapTo(updatedProduct.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred while updating an product.");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product by ID", description = "Delete a product by providing its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            boolean deletionResult = productService.deleteProduct(productId);

            if (deletionResult) {
                return ResponseEntity.ok().body("Product deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("An error occurred while deleting an product " + productId + ".");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleWrongDateFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Invalid available from or available to date format. Please provide the date in yyyy-MM-dd format");
    }
}
