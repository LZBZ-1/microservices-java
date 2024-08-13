package com.lzbz.client.controller;

import com.lzbz.client.dto.ClientRequest;
import com.lzbz.client.dto.ClientResponse;
import com.lzbz.client.service.ClientService;
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
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "API for managing clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided data. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "409", description = "Unique code already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get a client", description = "Retrieves a client by their unique code. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<ClientResponse>> getClient(
            @Parameter(description = "Unique code of the client") @PathVariable Long codigoUnico) {
        return clientService.getClientByCodigoUnico(codigoUnico)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/secret/{encryptedCodigoUnico}")
    @Operation(summary = "Get a client using encrypted code", description = "Retrieves a client by their encrypted unique code. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<ClientResponse>> getClientEncrypted(
            @Parameter(description = "Encrypted unique code of the client") @PathVariable String encryptedCodigoUnico) {
        return clientService.getClientByCodigoUnicoEncrypted(encryptedCodigoUnico)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Flux<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{codigoUnico}")
    @Operation(summary = "Update a client", description = "Updates the data of an existing client. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<ClientResponse>> updateClient(
            @Parameter(description = "Unique code of the client") @PathVariable Long codigoUnico,
            @RequestBody ClientRequest clientRequest) {
        return clientService.updateClient(codigoUnico, clientRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigoUnico}")
    @Operation(summary = "Delete a client", description = "Deletes a client by their unique code. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    public Mono<ResponseEntity<Void>> deleteClient(
            @Parameter(description = "Unique code of the client") @PathVariable Long codigoUnico) {
        return clientService.deleteClient(codigoUnico)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(ResponseStatusException.class, e -> {
                    if (e.getStatus() == HttpStatus.NOT_FOUND) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(e.getStatus()).build());
                });
    }
}