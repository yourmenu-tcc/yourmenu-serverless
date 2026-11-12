package tech.miqueias.bento.io.categoria.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CategoriaResponseDto(
    Long id, 
    Long restauranteId, 
    String nome, 
    String descricao
) {
}
