package tech.miqueias.bento.io.restaurante;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import tech.miqueias.bento.io.colaborador.UsuarioRestaurante;
import tech.miqueias.bento.io.usuario.Role;
import tech.miqueias.bento.io.usuario.Usuario;

class VinculoRestauranteTest {

    @Test
    void deveCriarVinculosSeparadosParaColaboracaoEConsumo() {
        Usuario usuario = new Usuario("Maria", "maria@email.com", "senha123", Set.of(Role.ADMINISTRADOR));
        Restaurante restaurante = new Restaurante(
                "Ponto do Sabor",
                "Restaurante testado",
                "Rua A, 123",
                LocalTime.of(11, 0),
                LocalTime.of(22, 0),
                30,
                "logo.png",
                "banner.png",
                "ponto-do-sabor"
        );

        UsuarioRestaurante colaboracao = new UsuarioRestaurante(usuario, restaurante, Role.GERENTE, StatusVinculoRestaurante.ATIVO);

        assertEquals(usuario, colaboracao.getUsuario());
        assertEquals(restaurante, colaboracao.getRestaurante());
        assertEquals(Role.GERENTE, colaboracao.getPapel());
        assertEquals(StatusVinculoRestaurante.ATIVO, colaboracao.getStatus());
    }
}
