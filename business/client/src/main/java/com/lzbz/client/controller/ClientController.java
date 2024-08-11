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
    public Mono<ResponseEntity<ClientResponse>> crearClient(@RequestBody ClientRequest clientRequest) {
        return clientService.crearClient(clientRequest)
                .map(client -> ResponseEntity.status(HttpStatus.CREATED).body(client))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("código único")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/{codigoUnico}")
    public Mono<ResponseEntity<ClientResponse>> obtenerClient(@PathVariable String codigoUnico) {
        return clientService.obtenerClientPorCodigoUnico(codigoUnico)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<ClientResponse> obtenerTodosLosClients() {
        return clientService.obtenerTodosLosClients();
    }

    @PutMapping("/{codigoUnico}")
    public Mono<ResponseEntity<ClientResponse>> actualizarClient(@PathVariable String codigoUnico, @RequestBody ClientRequest clientRequest) {
        return clientService.actualizarClient(codigoUnico, clientRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigoUnico}")
    public Mono<ResponseEntity<Void>> eliminarClient(@PathVariable String codigoUnico) {
        return clientService.eliminarClient(codigoUnico)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}