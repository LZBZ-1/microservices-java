package com.lzbz.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO for product responses")
public class ProductResponse extends BaseProductDTO {
    // No additional fields needed, as it inherits everything from BaseProductDTO
}
