package com.lzbz.client.dto;

import lombok.Data;

@Data
public class ClientRequest {
    private String codigoUnico;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
}