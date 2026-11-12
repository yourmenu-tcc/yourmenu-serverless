package tech.miqueias.bento.io.pedido.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record EnderecoPedidoRequestDto(
        @NotNull
        Long zonaDeEntregaId,

        @NotBlank
        String cep,

        @NotBlank
        String cidade,

        @NotBlank
        String rua,

        @NotBlank
        String numero,

        @NotBlank
        String bairro,

        String complemento,
        String referencia
) {}
