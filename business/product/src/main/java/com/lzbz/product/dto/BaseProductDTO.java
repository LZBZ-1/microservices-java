package com.lzbz.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class BaseProductDTO {
    private Long codigoProducto;
    private Long codigoUnico;
    private String tipo;
    private String nombre;
    private BigDecimal saldo;
}
