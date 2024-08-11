package com.lzbz.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @Indexed(unique = true)
    private Long codigoProducto;

    @Indexed
    private Long codigoUnico;

    @Indexed
    private String tipo;

    private String nombre;
    private BigDecimal saldo;

}