package tech.miqueias.bento.io.pratos.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
public record PratoRequestDto(
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull Long categoriaId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal preco,
        String imagemUrl
) {
}
