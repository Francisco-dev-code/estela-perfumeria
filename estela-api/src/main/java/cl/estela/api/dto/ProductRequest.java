package cl.estela.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String brand,
        @Size(max = 600) String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @Min(0) int stock) {
}
