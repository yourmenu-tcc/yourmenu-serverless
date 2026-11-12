package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.List;

@RegisterForReflection
public record ItemPedidoResponseDto(
        Long id,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal,
        List<ProdutoItemResponseDto> produtos
) {}
