package tech.miqueias.bento.io.auth.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record LoginResponse(
        String token,
        String tipo,
        long expiracaoEmSegundos
) {
    public LoginResponse(String token, long expiracaoEmSegundos) {
        this(token, "Bearer", expiracaoEmSegundos);
    }
}
