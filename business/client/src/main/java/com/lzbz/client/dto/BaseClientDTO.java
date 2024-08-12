package com.lzbz.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Base DTO for clients")
public abstract class BaseClientDTO {
    @Schema(description = "Unique client code", example = "1001")
    private Long codigoUnico;

    @Schema(description = "Client's first name(s)", example = "John")
    private String nombres;

    @Schema(description = "Client's last name(s)", example = "Doe")
    private String apellidos;

    @Schema(description = "Type of identification document", example = "PASSPORT")
    private String tipoDocumento;

    @Schema(description = "Identification document number", example = "AB123456")
    private String numeroDocumento;
}
