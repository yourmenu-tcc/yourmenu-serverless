package tech.miqueias.bento.io.colaborador;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.colaborador.dto.*;
import tech.miqueias.bento.io.restaurante.*;
import tech.miqueias.bento.io.shared.exception.*;
import tech.miqueias.bento.io.shared.tenant_context.AcessoRestauranteService;
import tech.miqueias.bento.io.usuario.*;

@ApplicationScoped public class UsuarioRestauranteService {

    @Inject
    UsuarioRestauranteRepository vinculos; 
    
    @Inject 
    UsuarioRepository usuarios; 
    
    @Inject 
    RestauranteRepository restaurantes; 
    
    @Inject 
    AcessoRestauranteService acesso;

    public List<ColaboradorResponseDto> listar(Long restauranteId) { 
        acesso.exigirAcesso(restauranteId, Role.GERENTE); 
        return vinculos.listarPorRestaurante(restauranteId).stream().map(this::dto).toList(); 
    }

    @Transactional 
    public ColaboradorResponseDto criar(Long restauranteId, ColaboradorRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE); 
        validarPapel(r.papel());

        if (vinculos.buscar(r.usuarioId(), restauranteId)
            .isPresent()) throw new DomainException("O usuário já possui vínculo com este restaurante.");

        Usuario usuario = usuarios
                            .buscarPorId(r.usuarioId())
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", r.usuarioId()));

        Restaurante restaurante = restaurantes
                            .buscarPorId(restauranteId)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante", restauranteId));

        UsuarioRestaurante vinculo = new UsuarioRestaurante(
            usuario, 
            restaurante, 
            r.papel(), 
            r.status()
        );
        vinculos.persist(vinculo); 
        
        return dto(vinculo);
    }

    @Transactional 
    public ColaboradorResponseDto atualizar(Long restauranteId, Long id, ColaboradorAtualizacaoRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE); 
        validarPapel(r.papel()); 

        UsuarioRestaurante vinculo = encontrar(restauranteId, id); 
        validarUltimoGerente(restauranteId, vinculo, r.papel(), r.status()); 

        vinculo.atualizar(r.papel(), r.status()); 
        
        return dto(vinculo);
    }
    @Transactional 
    public void excluir(Long restauranteId, Long id) { 
        acesso.exigirAcesso(restauranteId, Role.GERENTE); 
        UsuarioRestaurante vinculo = encontrar(restauranteId, id); 
        validarUltimoGerente(restauranteId, vinculo, null, null); 
        
        vinculos.delete(vinculo); 
    }

    // Métodos auxiliares privados
    private void validarPapel(Role papel) { 
        if (papel == Role.ADMINISTRADOR) throw new DomainException("Administrador é um papel global e não pode ser vinculado ao restaurante."); 
    }
    
    private void validarUltimoGerente(
        Long restauranteId, 
        UsuarioRestaurante atual, 
        Role novoPapel, 
        StatusVinculoRestaurante novoStatus
        ) {
        if (atual.getPapel() == Role.GERENTE && atual.getStatus() == StatusVinculoRestaurante.ATIVO && (novoPapel != Role.GERENTE || novoStatus != StatusVinculoRestaurante.ATIVO) && vinculos.contarAtivosPorPapel(restauranteId, Role.GERENTE) <= 1) throw new DomainException("O restaurante deve possuir ao menos um gerente ativo.");
    }

    private UsuarioRestaurante encontrar(Long restauranteId, Long id) {
        return vinculos
                    .buscarPorIdERestaurante(id, restauranteId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo", id)); 
    }

    private ColaboradorResponseDto dto(UsuarioRestaurante v) { 
        Usuario u = v.getUsuario(); 
        return new ColaboradorResponseDto(
            v.getId(), 
            u.getId(), 
            u.getNome(), 
            u.getEmail(), 
            v.getPapel(), 
            v.getStatus()
        ); 
    }
}
