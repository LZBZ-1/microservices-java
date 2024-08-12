package com.lzbz.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO for client requests")
public class ClientRequest extends BaseClientDTO {
    // No additional fields needed, as it inherits everything from BaseClientDTO
}