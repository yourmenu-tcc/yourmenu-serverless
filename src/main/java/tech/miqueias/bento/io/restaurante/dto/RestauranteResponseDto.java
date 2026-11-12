package tech.miqueias.bento.io.restaurante.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalTime;

@RegisterForReflection
public record RestauranteResponseDto(
        Long id,
        String nome,
        String descricao,
        boolean aberto,
        String endereco,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Integer tempoDeEntrega,
        String logoUrl,
        String bannerUrl,
        String acessoUrl
) {
}
