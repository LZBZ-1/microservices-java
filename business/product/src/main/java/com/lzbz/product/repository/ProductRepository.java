package com.lzbz.product.repository;

import com.lzbz.product.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String>  {
    Mono<Product> findByCodigoProducto(Long codigoProducto);
    Flux<Product> findByCodigoUnico(Long codigoUnico);
}
