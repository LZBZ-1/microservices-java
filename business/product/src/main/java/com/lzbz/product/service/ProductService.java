package com.lzbz.product.service;

import com.lzbz.product.dto.ProductRequest;
import com.lzbz.product.dto.ProductResponse;
import com.lzbz.product.model.Product;
import com.lzbz.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        return productRepository.save(product)
                .map(this::mapToProductResponse)
                .onErrorResume(DuplicateKeyException.class, ex ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "A Product with this unique code already exists"))
                );
    }

    public Mono<ProductResponse> getProductByCodigoProducto(Long codigoProducto) {
        return productRepository.findByCodigoProducto(codigoProducto)
                .map(this::mapToProductResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    public Flux<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .map(this::mapToProductResponse);
    }

    public Flux<ProductResponse> getAllProductsByCodigoUnico(Long codigoUnico) {
        return productRepository.findByCodigoUnico(codigoUnico)
                .map(this::mapToProductResponse)
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found with the given codigoUnico")));
    }

    public Mono<ProductResponse> updateProduct(Long codigoProducto, ProductRequest productRequest) {
        return productRepository.findByCodigoProducto(codigoProducto)
                .flatMap(product -> {
                    product.setCodigoUnico(productRequest.getCodigoUnico());
                    product.setTipo(productRequest.getTipo());
                    product.setNombre(productRequest.getNombre());
                    product.setSaldo(productRequest.getSaldo());
                    return productRepository.save(product);
                })
                .map(this::mapToProductResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    public Mono<Void> deleteProduct(Long codigoProducto) {
        return productRepository.findByCodigoProducto(codigoProducto)
                .flatMap(product -> productRepository.delete(product))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setCodigoProducto(product.getCodigoProducto());
        response.setCodigoUnico(product.getCodigoUnico());
        response.setTipo(product.getTipo());
        response.setNombre(product.getNombre());
        response.setSaldo(product.getSaldo());
        return response;
    }

    private Product mapToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setCodigoProducto(productRequest.getCodigoProducto());
        product.setCodigoUnico(productRequest.getCodigoUnico());
        product.setTipo(productRequest.getTipo());
        product.setNombre(productRequest.getNombre());
        product.setSaldo(productRequest.getSaldo());
        return product;
    }
}