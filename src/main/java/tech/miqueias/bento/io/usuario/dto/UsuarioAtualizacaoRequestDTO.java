package tech.miqueias.bento.io.usuario.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Set;
import jakarta.validation.constraints.*;
import tech.miqueias.bento.io.usuario.Role;
@RegisterForReflection
public record UsuarioAtualizacaoRequestDTO(@NotBlank String nome, @NotBlank @Email String email, @NotEmpty Set<Role> roles) { }
