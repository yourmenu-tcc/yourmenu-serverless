package tech.miqueias.bento.io.entrega.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public record ZonaDeEntregaResponseDto(
        Long id,
        Long restauranteId,
        String localizacao,
        BigDecimal valor
) {
}
