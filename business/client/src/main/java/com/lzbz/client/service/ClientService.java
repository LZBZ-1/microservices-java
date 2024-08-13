package com.lzbz.client.service;

import com.lzbz.client.common.Tracked;
import com.lzbz.client.common.TrackingContext;
import com.lzbz.client.dto.ClientRequest;
import com.lzbz.client.dto.ClientResponse;
import com.lzbz.client.model.Client;
import com.lzbz.client.repository.ClientRepository;
import com.lzbz.client.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Mono<ClientResponse> createClient(ClientRequest clientRequest) {
        Client client = mapToClient(clientRequest);
        return clientRepository.save(client)
                .map(this::mapToClientResponse)
                .onErrorResume(DuplicateKeyException.class, ex ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "A client with this unique code already exists"))
                );
    }

    public Mono<ClientResponse> getClientByCodigoUnico(Long codigoUnico) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .map(this::mapToClientResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")));
    }

    @Tracked
    public Mono<ClientResponse> getClientByCodigoUnicoEncrypted(String encryptedCodigoUnico) {
        return Mono.fromCallable(() -> {
            String trackingId = TrackingContext.getTrackingId();
            if (trackingId == null || trackingId.isEmpty()) {
                trackingId = TrackingContext.initializeTrackingId();
            }
            return trackingId;
        }).flatMap(trackingId -> {
            Long codigoUnico = EncryptionUtil.decrypt(encryptedCodigoUnico);
            return clientRepository.findByCodigoUnico(codigoUnico)
                    .map(this::mapToClientResponse)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")))
                    .doFinally(signalType -> TrackingContext.clearTrackingId());
        });
    }

    public Flux<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .map(this::mapToClientResponse);
    }

    public Mono<ClientResponse> updateClient(Long codigoUnico, ClientRequest clientRequest) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .flatMap(client -> {
                    client.setNombres(clientRequest.getNombres());
                    client.setApellidos(clientRequest.getApellidos());
                    client.setTipoDocumento(clientRequest.getTipoDocumento());
                    client.setNumeroDocumento(clientRequest.getNumeroDocumento());
                    return clientRepository.save(client);
                })
                .map(this::mapToClientResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")));
    }

    public Mono<Void> deleteClient(Long codigoUnico) {
        return clientRepository.findByCodigoUnico(codigoUnico)
                .flatMap(client -> clientRepository.delete(client))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found")));
    }

    private ClientResponse mapToClientResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setCodigoUnico(client.getCodigoUnico());
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