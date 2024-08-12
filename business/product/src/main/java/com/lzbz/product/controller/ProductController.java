package com.lzbz.product.controller;

import com.lzbz.product.dto.ProductRequest;
import com.lzbz.product.dto.ProductResponse;
import com.lzbz.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "API for managing products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided data. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "409", description = "Unique code already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("unique code")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/{codigoProducto}")
    @Operation(summary = "Get a product", description = "Retrieves a product by its unique code. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<ProductResponse>> getProduct(
            @Parameter(description = "Unique code of the product") @PathVariable Long codigoProducto) {
        return productService.getProductByCodigoProducto(codigoProducto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all products. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Flux<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/byCodigoUnico/{codigoUnico}")
    @Operation(summary = "Get all products by client code", description = "Retrieves a list of all products for a specific client. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Flux<ProductResponse> getAllProductsByCodigoUnico(
            @Parameter(description = "Unique code of the client") @PathVariable Long codigoUnico) {
        return productService.getAllProductsByCodigoUnico(codigoUnico);
    }

    @PutMapping("/{codigoProducto}")
    @Operation(summary = "Update a product", description = "Updates the data of an existing product. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<ProductResponse>> updateProduct(
            @Parameter(description = "Unique code of the product") @PathVariable Long codigoProducto,
            @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(codigoProducto, productRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigoProducto}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its unique code. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<Void>> deleteProduct(
            @Parameter(description = "Unique code of the product") @PathVariable Long codigoProducto) {
        return productService.deleteProduct(codigoProducto)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(ResponseStatusException.class, e -> {
                    if (e.getStatus() == HttpStatus.NOT_FOUND) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(e.getStatus()).build());
                });
    }
}