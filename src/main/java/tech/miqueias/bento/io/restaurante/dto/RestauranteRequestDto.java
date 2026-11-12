package tech.miqueias.bento.io.restaurante.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import tech.miqueias.bento.io.usuario.dto.UsuarioRequestDTO;

import java.time.LocalTime;

@RegisterForReflection
public record RestauranteRequestDto(
        Long gerenteId,
        UsuarioRequestDTO gerenteEntidade,
        String nome,
        String descricao,
        Boolean aberto,
        String endereco,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Integer tempoDeEntrega,
        String logoUrl,
        String bannerUrl
) {
}
