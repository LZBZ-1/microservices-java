package com.lzbz.client.repository;

import com.lzbz.client.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    Mono<Client> findByCodigoUnico(String codigoUnico);
}
