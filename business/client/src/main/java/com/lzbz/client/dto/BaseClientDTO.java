package com.lzbz.client.dto;

import lombok.Data;

@Data
public abstract class BaseClientDTO {
    private Long codigoUnico;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
}
