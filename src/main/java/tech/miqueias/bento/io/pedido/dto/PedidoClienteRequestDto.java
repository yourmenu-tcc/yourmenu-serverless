package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.NotBlank;

@RegisterForReflection
public record PedidoClienteRequestDto(
        @NotBlank
        String primeiroNome,

        String ultimoNome,

        @NotBlank
        String telefone
) {}
