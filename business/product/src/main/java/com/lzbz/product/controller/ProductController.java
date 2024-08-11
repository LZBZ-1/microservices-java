package com.lzbz.product.controller;

import com.lzbz.product.dto.ProductRequest;
import com.lzbz.product.dto.ProductResponse;
import com.lzbz.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest)
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product))
                .onErrorResume(e-> {
                    if (e.getMessage().contains("unique code")) {
                       return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/{codigoProducto}")
    public Mono<ResponseEntity<ProductResponse>> getProduct(@PathVariable Long codigoProducto) {
        return productService.getProductByCodigoProducto(codigoProducto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/byCodigoUnico/{codigoUnico}")
    public Flux<ProductResponse> getAllProductsByCodigoUnico(@PathVariable Long codigoUnico) {
        return productService.getAllProductsByCodigoUnico(codigoUnico);
    }

    @PutMapping("/{codigoProducto}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct(@PathVariable Long codigoProducto, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(codigoProducto, productRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigoProducto}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long codigoProducto) {
        return productService.deleteProduct(codigoProducto)
                .map(deleted -> deleted ? ResponseEntity.noContent().<Void>build()
                        : ResponseEntity.notFound().build());
    }
}
