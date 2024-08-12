package com.lzbz.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Base DTO for products")
public abstract class BaseProductDTO {
    @Schema(description = "Product code", example = "1001")
    private Long codigoProducto;

    @Schema(description = "Unique client code", example = "5001")
    private Long codigoUnico;

    @Schema(description = "Product type", example = "CHECKING_ACCOUNT")
    private String tipo;

    @Schema(description = "Product name", example = "Standard Checking Account")
    private String nombre;

    @Schema(description = "Product balance", example = "1000.50")
    private BigDecimal saldo;
}
