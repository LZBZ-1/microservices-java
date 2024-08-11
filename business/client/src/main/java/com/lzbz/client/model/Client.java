package com.lzbz.client.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "clientes")
public class Client {
    @Id
    private String id;

    @Indexed(unique = true)
    private String codigoUnico;

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
}
