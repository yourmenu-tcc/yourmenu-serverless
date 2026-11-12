package tech.miqueias.bento.io.colaborador.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import tech.miqueias.bento.io.restaurante.StatusVinculoRestaurante;
import tech.miqueias.bento.io.usuario.Role;

@RegisterForReflection
public record ColaboradorResponseDto(
    Long id, 
    Long usuarioId, 
    String nome, 
    String email, 
    Role papel, 
    StatusVinculoRestaurante status
) {
}
