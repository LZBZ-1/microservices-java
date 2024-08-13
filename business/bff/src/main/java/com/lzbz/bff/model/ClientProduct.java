package com.lzbz.bff.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ClientProduct {
    private Long codigoUnico;
    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private List<ProductInfo> productos;

    @Data
    public static class ProductInfo {
        private Long codigoProducto;
        private String tipo;
        private String nombre;
        private BigDecimal saldo;
    }
}
