package com.lzbz.client.dto;

import lombok.Data;

@Data
public class ClientResponse {
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
}
