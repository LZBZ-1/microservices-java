package com.lzbz.client.service;

import com.lzbz.client.dto.ClientRequest;
import com.lzbz.client.dto.ClientResponse;
import com.lzbz.client.model.Client;
import com.lzbz.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Mono<ClientResponse> crearClient(ClientRequest clientRequest) {
        Client client = mapToClient(clientRequest);
        return clientRepository.save(client)
                .map(this::mapToClientResponse)
                .onErrorResume(DuplicateKeyException.class, ex ->
                        Mono.error(new RuntimeException("A client with this unique code already exists"))
                );
    }

    public Mono<ClientResponse> obtenerClientPorCodigoUnico(String codigoUnico) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .map(this::mapToClientResponse)
                .switchIfEmpty(Mono.error(new RuntimeException("Client not found.")));
    }

    public Flux<ClientResponse> obtenerTodosLosClients() {
        return clientRepository.findAll()
                .map(this::mapToClientResponse);
    }

    public Mono<ClientResponse> actualizarClient(String codigoUnico, ClientRequest clientRequest) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .flatMap(client -> {
                    client.setNombres(clientRequest.getNombres());
                    client.setApellidos(clientRequest.getApellidos());
                    client.setTipoDocumento(clientRequest.getTipoDocumento());
                    client.setNumeroDocumento(clientRequest.getNumeroDocumento());
                    return clientRepository.save(client);
                })
                .map(this::mapToClientResponse)
                .switchIfEmpty(Mono.error(new RuntimeException("Client not found.")));
    }

    public Mono<Void> eliminarClient(String codigoUnico) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .flatMap(clientRepository::delete)
                .switchIfEmpty(Mono.error(new RuntimeException("Client not found.")));
    }

    private ClientResponse mapToClientResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setNombres(client.getNombres());
        response.setApellidos(client.getApellidos());
        response.setTipoDocumento(client.getTipoDocumento());
        response.setNumeroDocumento(client.getNumeroDocumento());
        return response;
    }

    private Client mapToClient(ClientRequest clientRequest) {
        Client client = new Client();
        client.setCodigoUnico(clientRequest.getCodigoUnico());
        client.setNombres(clientRequest.getNombres());
        client.setApellidos(clientRequest.getApellidos());
        client.setTipoDocumento(clientRequest.getTipoDocumento());
        client.setNumeroDocumento(clientRequest.getNumeroDocumento());
        return client;
    }
}