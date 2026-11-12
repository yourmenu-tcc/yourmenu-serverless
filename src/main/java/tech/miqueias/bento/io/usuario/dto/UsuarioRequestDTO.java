package tech.miqueias.bento.io.usuario.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import tech.miqueias.bento.io.usuario.Role;

import java.util.Set;

@RegisterForReflection
public record UsuarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha,

        @NotEmpty(message = "Pelo menos um role é obrigatório")
        Set<Role> roles
) {
}
