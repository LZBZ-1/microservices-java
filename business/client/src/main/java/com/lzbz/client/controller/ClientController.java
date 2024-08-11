package com.lzbz.client.controller;

import com.lzbz.client.dto.ClientRequest;
import com.lzbz.client.dto.ClientResponse;
import com.lzbz.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public Mono<ResponseEntity<ClientResponse>> createClient(@RequestBody ClientRequest clientRequest) {
        return clientService.createClient(clientRequest)
                .map(client -> ResponseEntity.status(HttpStatus.CREATED).body(client))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("unique code")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/{codigoUnico}")
    public Mono<ResponseEntity<ClientResponse>> getClient(@PathVariable Long codigoUnico) {
        return clientService.getClientByCodigoUnico(codigoUnico)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{codigoUnico}")
    public Mono<ResponseEntity<ClientResponse>> updateClient(@PathVariable Long codigoUnico, @RequestBody ClientRequest clientRequest) {
        return clientService.updateClient(codigoUnico, clientRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigoUnico}")
    public Mono<ResponseEntity<Void>> deleteClient(@PathVariable Long codigoUnico) {
        return clientService.deleteClient(codigoUnico)
                .map(deleted -> deleted ? ResponseEntity.noContent().<Void>build()
                        : ResponseEntity.notFound().build());
    }
}