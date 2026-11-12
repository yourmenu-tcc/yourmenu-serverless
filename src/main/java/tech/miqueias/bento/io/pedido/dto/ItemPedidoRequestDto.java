package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record ItemPedidoRequestDto(
        @NotNull
        @Min(1)
        Integer quantidade,

        @NotEmpty
        List<@Valid ProdutoItemRequestDto> produtos
) {}
