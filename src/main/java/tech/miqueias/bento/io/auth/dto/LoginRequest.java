package tech.miqueias.bento.io.auth.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@RegisterForReflection
public record LoginRequest(
        @NotEmpty(message = "O usuário deve ser informado.")
        @Email(message = "Um email válido deve ser informado.")
        String email,

        @NotEmpty(message = "O usuário deve ser informado.")
        @Size(min = 6, message = "A senha informada deve possuir pelo menos 6 caracteres.")
        String senha
) {
}
