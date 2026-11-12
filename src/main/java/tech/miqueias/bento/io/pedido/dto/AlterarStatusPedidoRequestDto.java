package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.NotNull;
import tech.miqueias.bento.io.pedido.StatusPedido;

@RegisterForReflection
public record AlterarStatusPedidoRequestDto(
        @NotNull
        StatusPedido status
) {}
