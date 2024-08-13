package com.lzbz.bff.controller;

import com.lzbz.bff.common.Tracked;
import com.lzbz.bff.common.TrackingContext;
import com.lzbz.bff.model.ClientProduct;
import com.lzbz.bff.service.BffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff")
public class BffController {

    private final BffService bffService;

    @Autowired
    public BffController(BffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping("/clients/{codigoUnico}")
    @Tracked
    public Mono<ClientProduct> getClientWithProducts(@PathVariable Long codigoUnico) {
        String trackingId = TrackingContext.initializeTrackingId();
        return bffService.getClientWithProducts(codigoUnico, trackingId);
    }
}