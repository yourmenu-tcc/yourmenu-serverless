package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.miqueias.bento.io.pedido.TipoEntrega;

@RegisterForReflection
public record PedidoRequestDto(
        @NotNull
        TipoEntrega tipoEntrega,

        @NotNull
        @Valid PedidoClienteRequestDto cliente,

        @Valid
        EnderecoPedidoRequestDto endereco,

        @NotEmpty
        List<@Valid ItemPedidoRequestDto> itens
) {}
