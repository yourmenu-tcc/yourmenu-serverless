package tech.miqueias.bento.io.shared.tenant_context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import tech.miqueias.bento.io.shared.exception.AcessoNegadoException;
import tech.miqueias.bento.io.shared.exception.DomainExceptionMapper;
import tech.miqueias.bento.io.usuario.Role;
import tech.miqueias.bento.io.usuario.Usuario;

class AcessoRestauranteServiceTest {
    private final TenantContext context = new TenantContext();
    private final AcessoRestauranteService acesso = new AcessoRestauranteService(context);

    @AfterEach
    void limparContexto() {
        context.clear();
    }

    @Test
    void gerenteDeOutroRestauranteRecebeForbidden() {
        context.bind(usuario(Role.GERENTE), 2L, Role.GERENTE);

        AcessoNegadoException erro = assertThrows(AcessoNegadoException.class,
                () -> acesso.exigirAcesso(1L, Role.GERENTE));

        assertEquals(403, new DomainExceptionMapper().toResponse(erro).getStatus());
    }

    @Test
    void funcionarioNaoPodeExecutarOperacaoDeGerente() {
        context.bind(usuario(Role.FUNCIONARIO), 1L, Role.FUNCIONARIO);

        assertThrows(AcessoNegadoException.class,
                () -> acesso.exigirAcesso(1L, Role.GERENTE));
    }

    @Test
    void gerenteDoRestauranteEAdministradorGlobalPodemAcessar() {
        context.bind(usuario(Role.GERENTE), 1L, Role.GERENTE);
        assertDoesNotThrow(() -> acesso.exigirAcesso(1L, Role.GERENTE));

        context.clear();
        context.bind(usuario(Role.ADMINISTRADOR), null, null);
        assertDoesNotThrow(() -> acesso.exigirAcesso(2L, Role.GERENTE));
    }

    private static Usuario usuario(Role role) {
        return new Usuario("Teste", "teste@example.test", "senha", Set.of(role));
    }
}
