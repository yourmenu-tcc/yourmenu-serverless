package tech.miqueias.bento.io.categoria.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.NotBlank;

@RegisterForReflection
public record CategoriaRequestDto(
        @NotBlank String nome,
        String descricao
) {
}
