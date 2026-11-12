package tech.miqueias.bento.io.pratos.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public record PratoResponseDto(
        Long id,
        Long restauranteId,
        Long categoriaId,
        String nome,
        String descricao,
        BigDecimal preco,
        String imagemUrl
) {}
