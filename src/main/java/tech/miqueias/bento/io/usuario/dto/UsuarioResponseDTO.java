package tech.miqueias.bento.io.usuario.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import tech.miqueias.bento.io.usuario.Role;

import java.util.Set;

@RegisterForReflection
public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Set<Role> roles
) {
}
