package tech.miqueias.bento.io.colaborador.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.NotNull;
import tech.miqueias.bento.io.restaurante.StatusVinculoRestaurante;
import tech.miqueias.bento.io.usuario.Role;

@RegisterForReflection
public record ColaboradorRequestDto(
    @NotNull 
    Long usuarioId, 
    
    @NotNull 
    Role papel, 
    
    StatusVinculoRestaurante status
) {
}
