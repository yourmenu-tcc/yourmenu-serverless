package tech.miqueias.bento.io.entrega.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record ZonaDeEntregaRequestDto(
        @NotBlank String localizacao,
        @NotNull @DecimalMin("0.0") BigDecimal valor
) {
}
