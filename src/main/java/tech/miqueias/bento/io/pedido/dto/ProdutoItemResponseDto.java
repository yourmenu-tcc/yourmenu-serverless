package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public record ProdutoItemResponseDto(
        Long pratoId,
        String nomePrato,
        BigDecimal divisao,
        BigDecimal precoUnitario
) {}
